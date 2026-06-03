package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "viagem")
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modal", nullable = false)
    private Modal modal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_origem", nullable = false)
    private Cidade cidadeOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_destino", nullable = false)
    private Cidade cidadeDestino;

    @Column(nullable = false)
    private LocalDateTime partida;

    @Column(nullable = false)
    private LocalDateTime chegada;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
}
