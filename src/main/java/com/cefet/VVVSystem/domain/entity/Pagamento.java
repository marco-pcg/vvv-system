package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Integer idPagamento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_reserva", referencedColumnName = "codigo", nullable = false)
    private Reserva reserva;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false, length = 30)
    private String status;
}
