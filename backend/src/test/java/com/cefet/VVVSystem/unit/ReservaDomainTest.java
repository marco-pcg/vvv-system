package com.cefet.VVVSystem.unit;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Ticket;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservaDomainTest {

    @Test
    @DisplayName("instanciarTicket - Sucesso ao emitir ticket para reserva CONFIRMADA (RN12)")
    void instanciarTicket_ReservaConfirmada_Sucesso() {
        Reserva reserva = new Reserva();
        reserva.setId(100L);
        reserva.setStatus(StatusReserva.CONFIRMADA);

        Ticket ticket = reserva.instanciarTicket("TCK-999", "B12");

        assertNotNull(ticket);
        assertEquals("TCK-999", ticket.getNumero());
        assertEquals("B12", ticket.getAssento());
        assertEquals(reserva, ticket.getReserva());
        assertEquals(ticket, reserva.getTicket()); // Verifica associação bidirecional
    }

    @Test
    @DisplayName("instanciarTicket - Falha ao tentar emitir ticket para reserva PENDENTE (RN12)")
    void instanciarTicket_ReservaPendente_LancaExcecao() {
        Reserva reserva = new Reserva();
        reserva.setId(101L);
        reserva.setStatus(StatusReserva.PENDENTE);

        Exception ex = assertThrows(IllegalStateException.class, () -> 
            reserva.instanciarTicket("TCK-999", "B12")
        );
        assertEquals("O ticket só pode ser emitido para uma reserva com pagamento confirmado (Status: CONFIRMADA).", ex.getMessage());
    }

    @Test
    @DisplayName("confirmarPagamento - Altera o status da reserva para CONFIRMADA")
    void confirmarPagamento_Sucesso() {
        Reserva reserva = new Reserva();
        reserva.setStatus(StatusReserva.PENDENTE);

        reserva.confirmarPagamento();

        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());
    }
}
