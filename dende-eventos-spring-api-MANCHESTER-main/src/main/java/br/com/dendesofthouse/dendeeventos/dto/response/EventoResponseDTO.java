package br.com.dendesofthouse.dendeeventos.dto.response;

import br.com.dendesofthouse.dendeeventos.enums.ModalidadeEvento;
import br.com.dendesofthouse.dendeeventos.enums.TipoEvento;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponseDTO {
    private Long id;
    private String nome;
    private String paginaWeb;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    private TipoEvento tipoEvento;
    private ModalidadeEvento modalidade;
    private String local;
    private Integer capacidadeMaxima;
    private Double precoUnitarioIngresso;
    private Double taxaCancelamento;
    private boolean ativo;
    private int ingressosVendidos;
    private int vagasDisponiveis;
    private String organizador;
    private Long eventoPrincipalId;
    private String eventoPrincipalNome;
}
