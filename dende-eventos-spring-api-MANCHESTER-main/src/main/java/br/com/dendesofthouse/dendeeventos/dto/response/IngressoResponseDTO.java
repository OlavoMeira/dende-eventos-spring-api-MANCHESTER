package br.com.dendesofthouse.dendeeventos.dto.response;

import br.com.dendesofthouse.dendeeventos.enums.StatusIngresso;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngressoResponseDTO {
    private Long id;
    private String evento;
    private String dataEvento;
    private String dataCompra;
    private String dataCancelamento;
    private Double valorPago;
    private Double valorReembolsado;
    private StatusIngresso status;
    private boolean eventoAtivo;
    private boolean eventoFinalizado;
}
