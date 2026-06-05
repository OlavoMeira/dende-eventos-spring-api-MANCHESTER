package br.com.dendesofthouse.dendeeventos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngressoRequestDTO {

    @NotBlank(message = "Email do usuário é obrigatório")
    @Email(message = "Email deve ser válido")
    private String usuarioEmail;

    @NotNull(message = "Valor pago é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor pago deve ser maior que zero")
    private Double totalPago;
}
