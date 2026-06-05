package br.com.dendesofthouse.dendeeventos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "empresa")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 18)
    @EqualsAndHashCode.Include
    private String cnpj;

    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = false, length = 255)
    private String nomeFantasia;

    @Column(name = "data_abertura")
    private LocalDate dataAbertura;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false, unique = true)
    private Usuario organizador;
}