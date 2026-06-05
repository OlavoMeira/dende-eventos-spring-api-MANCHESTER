package br.com.dendesofthouse.dendeeventos.controller;

import br.com.dendesofthouse.dendeeventos.dto.request.OrganizadorRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.OrganizadorResponseDTO;
import br.com.dendesofthouse.dendeeventos.exception.*;
import br.com.dendesofthouse.dendeeventos.service.OrganizadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/organizadores")
@RequiredArgsConstructor
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    @PostMapping
    public ResponseEntity<?> cadastrarOrganizador(@Valid @RequestBody OrganizadorRequestDTO dto) {
        try {
            OrganizadorResponseDTO response = organizadorService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EmailJaCadastradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{organizadorId}")
    public ResponseEntity<?> alterarOrganizador(
            @PathVariable Long organizadorId,
            @Valid @RequestBody OrganizadorRequestDTO dto) {
        try {
            OrganizadorResponseDTO response = organizadorService.alterar(organizadorId, dto);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/{organizadorId}")
    public ResponseEntity<?> visualizarPerfil(@PathVariable Long organizadorId) {
        try {
            OrganizadorResponseDTO response = organizadorService.buscarPorId(organizadorId);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{organizadorId}/{status}")
    public ResponseEntity<?> alterarStatus(
            @PathVariable Long organizadorId,
            @PathVariable String status) {
        try {
            organizadorService.alterarStatus(organizadorId, status);
            return ResponseEntity.ok(Map.of("mensagem", "Status do organizador atualizado com sucesso!"));
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException | OrganizadorComEventosAtivosException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}