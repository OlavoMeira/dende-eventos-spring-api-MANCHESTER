package br.com.dendesofthouse.dendeeventos.controller;

import br.com.dendesofthouse.dendeeventos.dto.response.EventoResponseDTO;
import br.com.dendesofthouse.dendeeventos.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
@Tag(name = "Feed", description = "Endpoints públicos para visualização de eventos disponíveis")
public class FeedEventosController {

    private final EventoService eventoService;

    @GetMapping
    @Operation(summary = "Feed de eventos",
            description = "Retorna lista de eventos ativos com vagas disponíveis, ordenados por data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feed retornado com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum evento disponível no momento")
    })
    public ResponseEntity<List<EventoResponseDTO>> feedEventos() {
        List<EventoResponseDTO> response = eventoService.listarFeed();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
