package br.com.dendesofthouse.dendeeventos.controller;

import br.com.dendesofthouse.dendeeventos.dto.request.IngressoRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.IngressoResponseDTO;
import br.com.dendesofthouse.dendeeventos.exception.*;
import br.com.dendesofthouse.dendeeventos.service.IngressoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Ingressos", description = "Endpoints para compra e cancelamento de ingressos")
public class IngressoController {

    private final IngressoService ingressoService;

    @PostMapping("/organizadores/{organizadorId}/eventos/{eventoId}/ingressos")
    @Operation(summary = "Comprar ingresso",
            description = "Realiza a compra de um ingresso para um evento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingresso comprado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na compra - evento lotado, dados inválidos, etc"),
            @ApiResponse(responseCode = "404", description = "Evento ou usuário não encontrado")
    })
    public ResponseEntity<?> comprarIngresso(
            @Parameter(description = "ID do organizador", required = true, example = "1")
            @PathVariable Long organizadorId,
            @Parameter(description = "ID do evento", required = true, example = "1")
            @PathVariable Long eventoId,
            @Valid @RequestBody IngressoRequestDTO dto) {
        try {
            IngressoResponseDTO response = ingressoService.comprar(organizadorId, eventoId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException | EventoLotadoException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/usuarios/{usuarioId}/ingressos/{ingressoId}/cancelar")
    @Operation(summary = "Cancelar ingresso",
            description = "Cancela um ingresso comprado e calcula o valor de reembolso conforme taxa do evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingresso cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ingresso já cancelado ou não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    public ResponseEntity<?> cancelarIngresso(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long usuarioId,
            @Parameter(description = "ID do ingresso", required = true, example = "1")
            @PathVariable Long ingressoId) {
        try {
            IngressoResponseDTO response = ingressoService.cancelar(usuarioId, ingressoId);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException | IngressoJaCanceladoException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/usuarios/{usuarioId}/ingressos")
    @Operation(summary = "Listar ingressos do usuário",
            description = "Retorna todos os ingressos de um usuário, ordenados por status e data")
    public ResponseEntity<?> listarIngressosDoUsuario(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long usuarioId) {
        try {
            List<IngressoResponseDTO> response = ingressoService.listarDoUsuario(usuarioId);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}