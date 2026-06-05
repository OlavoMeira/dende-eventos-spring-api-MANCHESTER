package br.com.dendesofthouse.dendeeventos.service;

import br.com.dendesofthouse.dendeeventos.dto.request.OrganizadorRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.OrganizadorResponseDTO;
import br.com.dendesofthouse.dendeeventos.entity.Empresa;
import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import br.com.dendesofthouse.dendeeventos.enums.TipoUsuario;
import br.com.dendesofthouse.dendeeventos.exception.*;
import br.com.dendesofthouse.dendeeventos.mapper.OrganizadorMapper;
import br.com.dendesofthouse.dendeeventos.repository.EventoRepository;
import br.com.dendesofthouse.dendeeventos.repository.UsuarioRepository;
import br.com.dendesofthouse.dendeeventos.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrganizadorService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final EventoRepository eventoRepository;

    @Transactional
    public OrganizadorResponseDTO cadastrar(OrganizadorRequestDTO dto) {
        validarCamposObrigatorios(dto);

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail(), "organizador");
        }

        Usuario organizador = OrganizadorMapper.toModel(dto);
        organizador.setTipoUsuario(TipoUsuario.ORGANIZADOR);
        organizador = usuarioRepository.save(organizador);

        if (dto.getCnpj() != null && !dto.getCnpj().trim().isEmpty()) {
            Empresa empresa = new Empresa();
            empresa.setCnpj(dto.getCnpj());
            empresa.setRazaoSocial(dto.getRazaoSocial());
            empresa.setNomeFantasia(dto.getNomeFantasia());
            empresa.setOrganizador(organizador);
            empresaRepository.save(empresa);
            organizador.setEmpresa(empresa);
        }

        return OrganizadorMapper.toResponse(organizador);
    }

    @Transactional
    public OrganizadorResponseDTO alterar(Long organizadorId, OrganizadorRequestDTO dto) {
        Usuario existente = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador", organizadorId));

        if (!existente.getEmail().equals(dto.getEmail())) {
            throw new RegraDeNegocioException("Não é permitido alterar o e-mail do organizador.");
        }

        OrganizadorMapper.updateModel(dto, existente);

        if (dto.getCnpj() != null && !dto.getCnpj().trim().isEmpty()) {
            Empresa empresa = empresaRepository.findByOrganizadorId(organizadorId)
                    .orElse(new Empresa());
            empresa.setCnpj(dto.getCnpj());
            empresa.setRazaoSocial(dto.getRazaoSocial());
            empresa.setNomeFantasia(dto.getNomeFantasia());
            empresa.setOrganizador(existente);
            empresaRepository.save(empresa);
            existente.setEmpresa(empresa);
        }

        existente = usuarioRepository.save(existente);
        return OrganizadorMapper.toResponse(existente);
    }

    @Transactional(readOnly = true)
    public OrganizadorResponseDTO buscarPorId(Long organizadorId) {
        Usuario organizador = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador", organizadorId));
        return OrganizadorMapper.toResponse(organizador);
    }

    @Transactional
    public void alterarStatus(Long organizadorId, String status) {
        Usuario organizador = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador", organizadorId));

        if ("ativar".equalsIgnoreCase(status)) {
            organizador.setAtivo(true);
        } else if ("desativar".equalsIgnoreCase(status)) {
            if (eventoRepository.existsByOrganizadorIdAndAtivoTrueAndDataFimAfter(organizadorId, LocalDateTime.now())) {
                throw new OrganizadorComEventosAtivosException(organizadorId);
            }
            organizador.setAtivo(false);
        } else {
            throw new RegraDeNegocioException("Status inválido. Use 'ativar' ou 'desativar'.");
        }

        usuarioRepository.save(organizador);
    }

    private void validarCamposObrigatorios(OrganizadorRequestDTO dto) {
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