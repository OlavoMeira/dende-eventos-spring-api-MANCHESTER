package br.com.dendesofthouse.dendeeventos.service;

import br.com.dendesofthouse.dendeeventos.dto.request.UsuarioRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.UsuarioResponseDTO;
import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import br.com.dendesofthouse.dendeeventos.enums.TipoUsuario;
import br.com.dendesofthouse.dendeeventos.exception.EmailJaCadastradoException;
import br.com.dendesofthouse.dendeeventos.exception.RecursoNaoEncontradoException;
import br.com.dendesofthouse.dendeeventos.exception.RegraDeNegocioException;
import br.com.dendesofthouse.dendeeventos.mapper.UsuarioMapper;
import br.com.dendesofthouse.dendeeventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        validarCamposObrigatorios(dto);

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail(), "usuário");
        }

        Usuario usuario = UsuarioMapper.toModel(dto);
        usuario.setTipoUsuario(TipoUsuario.COMUM);
        usuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponseDTO alterar(Long usuarioId, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", usuarioId));

        if (!usuarioExistente.getEmail().equals(dto.getEmail())) {
            throw new RegraDeNegocioException("Não é permitido alterar o e-mail do usuário.");
        }

        UsuarioMapper.updateModel(dto, usuarioExistente);
        usuarioExistente = usuarioRepository.save(usuarioExistente);
        return UsuarioMapper.toResponse(usuarioExistente);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", usuarioId));
        return UsuarioMapper.toResponse(usuario);
    }

    @Transactional
    public void alterarStatus(Long usuarioId, String status) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", usuarioId));

        if ("ativar".equalsIgnoreCase(status)) {
            usuario.setAtivo(true);
        } else if ("desativar".equalsIgnoreCase(status)) {
            usuario.setAtivo(false);
        } else {
            throw new RegraDeNegocioException("Status inválido. Use 'ativar' ou 'desativar'.");
        }

        usuarioRepository.save(usuario);
    }

    private void validarCamposObrigatorios(UsuarioRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty())
            throw new RegraDeNegocioException("Nome é obrigatório.");
        if (dto.getDataNascimento() == null)
            throw new RegraDeNegocioException("Data de nascimento é obrigatória.");
        if (dto.getSexo() == null || dto.getSexo().trim().isEmpty())
            throw new RegraDeNegocioException("Sexo é obrigatório.");
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty())
            throw new RegraDeNegocioException("Email é obrigatório.");
        if (dto.getSenha() == null || dto.getSenha().trim().isEmpty())
            throw new RegraDeNegocioException("Senha é obrigatória.");
    }
}
