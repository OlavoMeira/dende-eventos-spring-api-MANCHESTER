package br.com.dendesofthouse.dendeeventos.service;

import br.com.dendesofthouse.dendeeventos.dto.request.EventoRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.EventoResponseDTO;
import br.com.dendesofthouse.dendeeventos.entity.Evento;
import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import br.com.dendesofthouse.dendeeventos.exception.RecursoNaoEncontradoException;
import br.com.dendesofthouse.dendeeventos.exception.RegraDeNegocioException;
import br.com.dendesofthouse.dendeeventos.mapper.EventoMapper;
import br.com.dendesofthouse.dendeeventos.repository.EventoRepository;
import br.com.dendesofthouse.dendeeventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public EventoResponseDTO cadastrar(Long organizadorId, EventoRequestDTO dto) {
        Usuario organizador = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador", organizadorId));

        if (!organizador.getAtivo()) {
            throw new RegraDeNegocioException("Organizador está desativado. Não é possível cadastrar eventos.");
        }

        validarCamposObrigatorios(dto);
        validarDatas(dto.getDataInicio(), dto.getDataFim());

        Evento eventoPrincipal = null;
        if (dto.getEventoPrincipalId() != null) {
            eventoPrincipal = eventoRepository.findById(dto.getEventoPrincipalId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Evento principal", dto.getEventoPrincipalId()));
        }

        Evento evento = EventoMapper.toModel(dto);
        evento.setOrganizador(organizador);
        evento.setEventoPrincipal(eventoPrincipal);
        evento.setAtivo(true);

        evento = eventoRepository.save(evento);
        return EventoMapper.toResponse(evento);
    }

    @Transactional
    public EventoResponseDTO alterar(Long organizadorId, Long eventoId, EventoRequestDTO dto) {
        usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador", organizadorId));

        Evento eventoExistente = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento", eventoId));

        if (!eventoExistente.getOrganizador().getId().equals(organizadorId)) {
            throw new RegraDeNegocioException("Este evento não pertence ao organizador informado.");
        }
        if (!eventoExistente.getAtivo()) {
            throw new RegraDeNegocioException("Não é possível alterar um evento inativo.");
        }
        if (dto.getDataInicio() != null && dto.getDataFim() != null) {
            validarDatas(dto.getDataInicio(), dto.getDataFim());
        }

        EventoMapper.updateModel(dto, eventoExistente);
        eventoExistente = eventoRepository.save(eventoExistente);
        return EventoMapper.toResponse(eventoExistente);
    }

    @Transactional
    public void alterarStatus(Long organizadorId, Long eventoId, String status) {
        usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador", organizadorId));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento", eventoId));

        if (!evento.getOrganizador().getId().equals(organizadorId)) {
            throw new RegraDeNegocioException("Este evento não pertence ao organizador informado.");
        }

        if ("ativar".equalsIgnoreCase(status)) {
            evento.setAtivo(true);
            eventoRepository.save(evento);
        } else if ("desativar".equalsIgnoreCase(status)) {
            evento.setAtivo(false);
            eventoRepository.save(evento);
        } else {
            throw new RegraDeNegocioException("Status inválido. Use 'ativar' ou 'desativar'.");
        }
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarDoOrganizador(Long organizadorId) {
        usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Organizador", organizadorId));

        return eventoRepository.findByOrganizadorId(organizadorId)
                .stream()
                .map(EventoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarFeed() {
        return eventoRepository.findEventosAtivosComVagas()
                .stream()
                .map(EventoMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void validarCamposObrigatorios(EventoRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty())
            throw new RegraDeNegocioException("Nome do evento é obrigatório.");
        if (dto.getDataInicio() == null)
            throw new RegraDeNegocioException("Data de início é obrigatória.");
        if (dto.getDataFim() == null)
            throw new RegraDeNegocioException("Data de fim é obrigatória.");
        if (dto.getTipoEvento() == null)
            throw new RegraDeNegocioException("Tipo de evento é obrigatório.");
        if (dto.getModalidade() == null)
            throw new RegraDeNegocioException("Modalidade é obrigatória.");
        if (dto.getCapacidadeMaxima() == null || dto.getCapacidadeMaxima() <= 0)
            throw new RegraDeNegocioException("Capacidade máxima deve ser maior que zero.");
        if (dto.getPrecoUnitarioIngresso() == null || dto.getPrecoUnitarioIngresso() < 0)
            throw new RegraDeNegocioException("Preço do ingresso é obrigatório e deve ser maior ou igual a zero.");
    }

    private void validarDatas(LocalDateTime dataInicio, LocalDateTime dataFim) {
        LocalDateTime agora = LocalDateTime.now();
        if (dataInicio.isBefore(agora))
            throw new RegraDeNegocioException("A data de início do evento não pode ser anterior à data atual.");
        if (dataFim.isBefore(dataInicio))
            throw new RegraDeNegocioException("A data de fim não pode ser anterior à data de início.");
        if (Duration.between(dataInicio, dataFim).toMinutes() < 30)
            throw new RegraDeNegocioException("O evento deve ter no mínimo 30 minutos de duração.");
    }
}
