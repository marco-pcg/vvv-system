package com.cefet.VVVSystem.unit;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Ticket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    @DisplayName("emitirTicket - Sucesso na emissão com dados completos (RN13)")
    void emitirTicket_Sucesso() {
        Reserva reserva = new Reserva();
        reserva.setId(10L);

        Ticket ticket = Ticket.emitirTicket("TCK-1234", "A01", reserva);

        assertNotNull(ticket);
        assertEquals("TCK-1234", ticket.getNumero());
        assertEquals("A01", ticket.getAssento());
        assertEquals(reserva, ticket.getReserva());
    }

    @Test
    @DisplayName("emitirTicket - Falha ao passar número nulo ou vazio")
    void emitirTicket_NumeroInvalido_LancaExcecao() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);

        Exception ex1 = assertThrows(IllegalArgumentException.class, () -> 
            Ticket.emitirTicket(null, "A01", reserva)
        );
        assertEquals("Número do ticket é obrigatório.", ex1.getMessage());

        Exception ex2 = assertThrows(IllegalArgumentException.class, () -> 
            Ticket.emitirTicket("  ", "A01", reserva)
        );
        assertEquals("Número do ticket é obrigatório.", ex2.getMessage());
    }

    @Test
    @DisplayName("emitirTicket - Falha ao passar assento nulo ou vazio")
    void emitirTicket_AssentoInvalido_LancaExcecao() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> 
            Ticket.emitirTicket("TCK-123", null, reserva)
        );
        assertEquals("Assento do ticket é obrigatório.", ex.getMessage());
    }

    @Test
    @DisplayName("emitirTicket - Falha ao passar reserva nula")
    void emitirTicket_ReservaNula_LancaExcecao() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> 
            Ticket.emitirTicket("TCK-123", "A01", null)
        );
        assertEquals("A reserva associada ao ticket é obrigatória.", ex.getMessage());
    }
}
