package br.com.dendesofthouse.dendeeventos.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizadorResponseDTO {
    private Long id;
    private String nome;
    private String dataNascimento;
    private String idadeCompleta;
    private String sexo;
    private String email;
    private boolean ativo;
    private EmpresaResponseDTO empresa;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmpresaResponseDTO {
        private String cnpj;
        private String razaoSocial;
        private String nomeFantasia;
    }
}
