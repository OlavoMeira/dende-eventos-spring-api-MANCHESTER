package br.com.dendesofthouse.dendeeventos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "evento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 500)
    private String paginaWeb;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "tipo_evento", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ModalidadeEvento modalidade;

    @Column(name = "local_evento", nullable = false, length = 500)
    private String local;

    @Column(name = "capacidade_maxima", nullable = false)
    private Integer capacidadeMaxima;

    @Column(name = "preco_ingresso", nullable = false)
    private Double precoUnitarioIngresso;

    @Column(name = "estorna_ingresso", nullable = false)
    private Boolean estornaIngresso = false;

    @Column(name = "taxa_estorno", nullable = false)
    private Double taxaCancelamento = 0.0;

    @Column(nullable = false)
    private Boolean ativo = false;

    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Usuario organizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_principal_id")
    private Evento eventoPrincipal;

    @OneToMany(mappedBy = "eventoPrincipal", cascade = CascadeType.ALL)
    private List<Evento> subEventos = new ArrayList<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    private List<Ingresso> ingressos = new ArrayList<>();

    public boolean isLotado() {
        if (capacidadeMaxima == null || capacidadeMaxima <= 0) return true;
        long ingressosAtivos = ingressos.stream()
                .filter(i -> i.getStatus() == StatusIngresso.ATIVO)
                .count();
        return ingressosAtivos >= capacidadeMaxima;
    }

    public boolean isFinalizado() {
        if (dataFim == null) return false;
        return dataFim.isBefore(LocalDateTime.now());
    }

    public boolean isEmAndamento() {
        if (dataInicio == null || dataFim == null) return false;
        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(dataInicio) && agora.isBefore(dataFim);
    }

    public int getIngressosVendidos() {
        if (ingressos == null) return 0;
        return (int) ingressos.stream()
                .filter(i -> i.getStatus() == StatusIngresso.ATIVO)
                .count();
    }

    public int getVagasDisponiveis() {
        if (capacidadeMaxima == null) return 0;
        return capacidadeMaxima - getIngressosVendidos();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(id, evento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
