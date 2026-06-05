package br.com.dendesofthouse.dendeeventos.service;

import br.com.dendesofthouse.dendeeventos.dto.request.IngressoRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.IngressoResponseDTO;
import br.com.dendesofthouse.dendeeventos.entity.Evento;
import br.com.dendesofthouse.dendeeventos.entity.Ingresso;
import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import br.com.dendesofthouse.dendeeventos.enums.StatusIngresso;
import br.com.dendesofthouse.dendeeventos.exception.*;
import br.com.dendesofthouse.dendeeventos.mapper.IngressoMapper;
import br.com.dendesofthouse.dendeeventos.repository.EventoRepository;
import br.com.dendesofthouse.dendeeventos.repository.IngressoRepository;
import br.com.dendesofthouse.dendeeventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public IngressoResponseDTO comprar(Long organizadorId, Long eventoId, IngressoRequestDTO dto) {
        if (dto.getUsuarioEmail() == null || dto.getUsuarioEmail().trim().isEmpty())
            throw new RegraDeNegocioException("Email do usuário é obrigatório.");
        if (dto.getTotalPago() == null || dto.getTotalPago() <= 0)
            throw new RegraDeNegocioException("Valor pago é obrigatório e deve ser maior que zero.");

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento", eventoId));

        if (!evento.getOrganizador().getId().equals(organizadorId))
            throw new RegraDeNegocioException("Este evento não pertence ao organizador informado.");
        if (!evento.getAtivo())
            throw new RegraDeNegocioException("Este evento não está ativo para venda de ingressos.");

        int ingressosAtivos = ingressoRepository.countIngressosAtivosByEventoId(eventoId);
        if (evento.getCapacidadeMaxima() != null && ingressosAtivos >= evento.getCapacidadeMaxima())
            throw new EventoLotadoException(evento.getNome());

        Usuario usuario = usuarioRepository.findByEmail(dto.getUsuarioEmail())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com e-mail " + dto.getUsuarioEmail() + " não encontrado."));

        if (!usuario.getAtivo())
            throw new RegraDeNegocioException("Usuário está desativado. Não é possível comprar ingressos.");

        // Valida valor total para sub-evento
        if (evento.getEventoPrincipal() != null) {
            Evento principal = evento.getEventoPrincipal();
            double valorTotal = principal.getPrecoUnitarioIngresso() + evento.getPrecoUnitarioIngresso();

            if (Math.abs(valorTotal - dto.getTotalPago()) > 0.01)
                throw new RegraDeNegocioException(
                        "Valor pago incorreto. O valor total deve ser: " + valorTotal);

            // Cria ingresso para o evento principal também
            Ingresso ingressoPrincipal = new Ingresso();
            ingressoPrincipal.setUsuario(usuario);
            ingressoPrincipal.setEvento(principal);
            ingressoPrincipal.setValorPago(principal.getPrecoUnitarioIngresso());
            ingressoRepository.save(ingressoPrincipal);
        }

        Ingresso ingresso = new Ingresso();
        ingresso.setUsuario(usuario);
        ingresso.setEvento(evento);
        ingresso.setValorPago(evento.getPrecoUnitarioIngresso());
        ingresso = ingressoRepository.save(ingresso);

        return IngressoMapper.toResponse(ingresso);
    }

    @Transactional
    public IngressoResponseDTO cancelar(Long usuarioId, Long ingressoId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", usuarioId));

        Ingresso ingresso = ingressoRepository.findById(ingressoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ingresso", ingressoId));

        if (!ingresso.getUsuario().getId().equals(usuarioId))
            throw new RegraDeNegocioException("Este ingresso não pertence ao usuário informado.");

        if (ingresso.getStatus() == StatusIngresso.CANCELADO) {
            throw new IngressoJaCanceladoException(ingressoId);
        }

        double taxa = ingresso.getEvento().getTaxaCancelamento();
        ingresso.cancelar(taxa);
        ingresso = ingressoRepository.save(ingresso);

        return IngressoMapper.toResponse(ingresso);
    }

    @Transactional(readOnly = true)
    public List<IngressoResponseDTO> listarDoUsuario(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", usuarioId));

        return ingressoRepository.findIngressosOrdenadosPorUsuario(usuarioId)
                .stream()
                .map(IngressoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
