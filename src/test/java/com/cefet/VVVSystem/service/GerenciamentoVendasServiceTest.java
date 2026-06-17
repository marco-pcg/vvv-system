package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Ticket;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GerenciamentoVendasServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private GerenciamentoVendasService gerenciamentoVendasService;

    private Reserva reserva;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setCodigo("RES-TESTE");
        reserva.setStatus(StatusReserva.AGUARDANDO_APROVACAO);

        ticket = new Ticket();
        ticket.setNumero("TKT-1234");
        ticket.setAssento("A1");
        ticket.setReserva(reserva);
    }

    @Test
    void aprovarVendaOnline_Sucesso() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);
        when(ticketService.emitirTicket(any(Reserva.class))).thenReturn(ticket);

        Ticket emitido = gerenciamentoVendasService.aprovarVendaOnline(1L);

        assertNotNull(emitido);
        assertEquals("TKT-1234", emitido.getNumero());
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());

        verify(reservaRepository).save(reserva);
        verify(ticketService).emitirTicket(reserva);
    }

    @Test
    void aprovarVendaOnline_StatusIncorreto_Falha() {
        reserva.setStatus(StatusReserva.PENDENTE);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        BusinessException exception = assertThrows(BusinessException.class, () -> 
            gerenciamentoVendasService.aprovarVendaOnline(1L)
        );

        assertTrue(exception.getMessage().contains("não está aguardando aprovação"));
        verify(reservaRepository, never()).save(any());
        verify(ticketService, never()).emitirTicket(any());
    }
}
