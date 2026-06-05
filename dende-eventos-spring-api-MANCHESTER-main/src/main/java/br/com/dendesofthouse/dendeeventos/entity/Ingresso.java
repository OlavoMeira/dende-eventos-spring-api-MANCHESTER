package br.com.dendesofthouse.dendeeventos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ingresso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "data_compra", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCompra;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "valor_pago", nullable = false)
    private Double valorPago;

    @Column(name = "valor_estornado", nullable = false)
    private Double valorReembolsado = 0.0;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private StatusIngresso status = StatusIngresso.ATIVO;

    public void cancelar(Double taxaCancelamento) {
        this.status = StatusIngresso.CANCELADO;
        this.dataCancelamento = LocalDateTime.now();
        if (taxaCancelamento > 0) {
            this.valorReembolsado = valorPago * (1 - taxaCancelamento);
        } else {
            this.valorReembolsado = valorPago;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingresso ingresso = (Ingresso) o;
        return Objects.equals(id, ingresso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ingresso{" +
                "id=" + id +
                ", evento=" + (evento != null ? evento.getNome() : "null") +
                ", status=" + status +
                '}';
    }
}
