package br.com.dendesofthouse.dendeeventos.controller;

import br.com.dendesofthouse.dendeeventos.dto.request.UsuarioRequestDTO;
import br.com.dendesofthouse.dendeeventos.dto.response.UsuarioResponseDTO;
import br.com.dendesofthouse.dendeeventos.exception.EmailJaCadastradoException;
import br.com.dendesofthouse.dendeeventos.exception.RecursoNaoEncontradoException;
import br.com.dendesofthouse.dendeeventos.exception.RegraDeNegocioException;
import br.com.dendesofthouse.dendeeventos.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EmailJaCadastradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<?> alterarUsuario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.alterar(usuarioId, dto);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> visualizarPerfil(@PathVariable Long usuarioId) {
        try {
            UsuarioResponseDTO response = usuarioService.buscarPorId(usuarioId);
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{usuarioId}/{status}")
    public ResponseEntity<?> alterarStatus(
            @PathVariable Long usuarioId,
            @PathVariable String status) {
        try {
            usuarioService.alterarStatus(usuarioId, status);
            return ResponseEntity.ok(Map.of("mensagem", "Status do usuário atualizado com sucesso!"));
        } catch (RecursoNaoEncontradoException | RegraDeNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}