package br.com.dendesofthouse.dendeeventos.dto.request;

import br.com.dendesofthouse.dendeeventos.enums.ModalidadeEvento;
import br.com.dendesofthouse.dendeeventos.enums.TipoEvento;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequestDTO {

    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    @Size(max = 500, message = "Página web deve ter no máximo 500 caracteres")
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
            message = "URL da página web inválida")
    private String paginaWeb;

    private String descricao;

    @NotNull(message = "Data de início é obrigatória")
    @Future(message = "Data de início deve ser futura")
    private LocalDateTime dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime dataFim;

    @NotNull(message = "Tipo de evento é obrigatório")
    private TipoEvento tipoEvento;

    @NotNull(message = "Modalidade é obrigatória")
    private ModalidadeEvento modalidade;

    @NotBlank(message = "Local é obrigatório")
    @Size(max = 500, message = "Local deve ter no máximo 500 caracteres")
    private String local;

    @NotNull(message = "Capacidade máxima é obrigatória")
    @Min(value = 1, message = "Capacidade máxima deve ser pelo menos 1")
    private Integer capacidadeMaxima;

    @NotNull(message = "Preço do ingresso é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "Preço do ingresso não pode ser negativo")
    private Double precoUnitarioIngresso;

    @DecimalMin(value = "0.0", message = "Taxa de cancelamento não pode ser negativa")
    @DecimalMax(value = "100.0", message = "Taxa de cancelamento não pode exceder 100%")
    private Double taxaCancelamento = 0.0;

    private Long eventoPrincipalId;
}
