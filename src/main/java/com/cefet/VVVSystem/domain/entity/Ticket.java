package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @Column(length = 20, nullable = false)
    private String numero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_reserva", referencedColumnName = "codigo", nullable = false)
    private Reserva reserva;

    @Column(nullable = false, length = 10)
    private String assento;
}
