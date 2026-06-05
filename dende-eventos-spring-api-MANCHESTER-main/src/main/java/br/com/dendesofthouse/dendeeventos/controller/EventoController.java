package br.com.dendesofthouse.dendeeventos.controller;

import br.com.dendesofthouse.dendeeventos.dto.request.EventoRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.EventoResponseDTO;
import br.com.dendesofthouse.dendeeventos.exception.RecursoNaoEncontradoException;
import br.com.dendesofthouse.dendeeventos.exception.RegraDeNegocioException;
import br.com.dendesofthouse.dendeeventos.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organizadores")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping("/{organizadorId}/eventos")
    public ResponseEntity<?> cadastrarEvento(
            @PathVariable Long organizadorId,
            @Valid @RequestBody EventoRequestDTO dto) {
        try {
            EventoResponseDTO response = eventoService.cadastrar(organizadorId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{organizadorId}/eventos/{eventoId}")
    public ResponseEntity<?> alterarEvento(
            @PathVariable Long organizadorId,
            @PathVariable Long eventoId,
            @Valid @RequestBody EventoRequestDTO dto) {
        try {
            EventoResponseDTO response = eventoService.alterar(organizadorId, eventoId, dto);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PatchMapping("/{organizadorId}/eventos/{eventoId}/{status}")
    public ResponseEntity<?> alterarStatusEvento(
            @PathVariable Long organizadorId,
            @PathVariable Long eventoId,
            @PathVariable String status) {
        try {
            eventoService.alterarStatus(organizadorId, eventoId, status);
            return ResponseEntity.ok(Map.of("mensagem", "Status do evento atualizado com sucesso!"));
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/{organizadorId}/eventos")
    public ResponseEntity<?> listarEventosDoOrganizador(@PathVariable Long organizadorId) {
        try {
            List<EventoResponseDTO> response = eventoService.listarDoOrganizador(organizadorId);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
