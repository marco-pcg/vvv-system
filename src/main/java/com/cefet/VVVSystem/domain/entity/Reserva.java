package com.cefet.VVVSystem.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.cefet.VVVSystem.domain.enums.StatusReserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viagem", nullable = false)
    private Viagem viagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passageiro", nullable = false)
    private Passageiro passageiro;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusReserva status;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @OneToOne(mappedBy = "reserva", cascade = jakarta.persistence.CascadeType.ALL, fetch = FetchType.LAZY)
    private Ticket ticket;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Reserva reserva = (Reserva) o;
        return codigo != null && Objects.equals(codigo, reserva.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    public void confirmarPagamento() {
        this.status = StatusReserva.CONFIRMADA;
    }

    public Ticket instanciarTicket(String numero, String assento) {
        if (this.status != StatusReserva.CONFIRMADA) {
            throw new IllegalStateException("O ticket só pode ser emitido para uma reserva com pagamento confirmado (Status: CONFIRMADA).");
        }
        Ticket novoTicket = Ticket.emitirTicket(numero, assento, this);
        this.ticket = novoTicket;
        return novoTicket;
    }
}
