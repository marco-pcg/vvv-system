package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @Column(nullable = false, length = 10)
    private String assento;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return numero != null && Objects.equals(numero, ticket.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    public static Ticket emitirTicket(String numero, String assento, Reserva reserva) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("Número do ticket é obrigatório.");
        }
        if (assento == null || assento.trim().isEmpty()) {
            throw new IllegalArgumentException("Assento do ticket é obrigatório.");
        }
        if (reserva == null) {
            throw new IllegalArgumentException("A reserva associada ao ticket é obrigatória.");
        }

        Ticket ticket = new Ticket();
        ticket.setNumero(numero);
        ticket.setAssento(assento);
        ticket.setReserva(reserva);
        return ticket;
    }
}
