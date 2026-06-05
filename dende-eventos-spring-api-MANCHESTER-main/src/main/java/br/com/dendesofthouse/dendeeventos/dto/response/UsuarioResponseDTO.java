package br.com.dendesofthouse.dendeeventos.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String dataNascimento;
    private String idadeCompleta;
    private String sexo;
    private String email;
    private boolean ativo;
}
