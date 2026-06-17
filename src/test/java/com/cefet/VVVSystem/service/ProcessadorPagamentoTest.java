package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.PagamentoDinheiro;
import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.*;
import com.cefet.VVVSystem.strategy.*;
import com.cefet.VVVSystem.domain.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProcessadorPagamentoTest {

    private ProcessadorPagamento processador;
    private com.cefet.VVVSystem.domain.repository.TicketRepository ticketRepositoryMock;

    @BeforeEach
    void setUp() {
        ticketRepositoryMock = org.mockito.Mockito.mock(com.cefet.VVVSystem.domain.repository.TicketRepository.class);
        // Instantiate the strategy list manually for a pure lightweight unit test
        List<PagamentoStrategy> estrategias = List.of(
            new PagamentoDinheiroStrategy(),
            new PagamentoDebitoStrategy(),
            new PagamentoCreditoStrategy()
        );
        processador = new ProcessadorPagamento(estrategias, ticketRepositoryMock);
    }

    @Test
    @DisplayName("Should automatically select Cash (Dinheiro) strategy and confirm reservation status")
    void testProcessamentoComSucessoDinheiro() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId(42L);
        reserva.setStatus(StatusReserva.PENDENTE);

        PagamentoDinheiro dinheiro = new PagamentoDinheiro();
        dinheiro.setValorRecebido(300.00);
        dinheiro.setTroco(50.00);

        // Act
        processador.processarERegistrarPagamento(reserva, dinheiro);

        // Assert
        assertEquals(StatusPagamento.APROVADO, dinheiro.getStatus(), "Payment status should transition to CONFIRMADO.");
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus(), "Reservation status should transition to CONFIRMADA.");
        assertNotNull(dinheiro.getReserva(), "Payment record should be successfully bound to the reservation.");
    }

    @Test
    @DisplayName("Should automatically select Credito strategy and not apply interest for up to 4 installments")
    void testProcessamentoComSucessoCreditoAte4Parcelas() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId(10L);
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setValorTotal(new java.math.BigDecimal("100.00"));

        PagamentoCredito credito = new PagamentoCredito();
        credito.setNumeroCartao("1234");
        credito.setParcelas(4);
        credito.setReserva(reserva);

        // Act
        processador.processarERegistrarPagamento(reserva, credito);

        // Assert
        assertEquals(StatusPagamento.APROVADO, credito.getStatus());
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());
        assertEquals(new java.math.BigDecimal("100.00"), reserva.getValorTotal(), "Value should not have interest applied.");
    }

    @Test
    @DisplayName("Should automatically select Credito strategy and apply 5% interest for more than 4 installments")
    void testProcessamentoComSucessoCreditoAcimaDe4Parcelas() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId(11L);
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setValorTotal(new java.math.BigDecimal("100.00"));

        PagamentoCredito credito = new PagamentoCredito();
        credito.setNumeroCartao("5678");
        credito.setParcelas(5);
        credito.setReserva(reserva);

        // Act
        processador.processarERegistrarPagamento(reserva, credito);

        // Assert
        assertEquals(StatusPagamento.APROVADO, credito.getStatus());
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());
        assertEquals(new java.math.BigDecimal("105.00"), reserva.getValorTotal(), "Value should have 5% interest applied.");
    }

    @Test
    @DisplayName("Should automatically select Debito strategy and process successfully")
    void testProcessamentoComSucessoDebito() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId(12L);
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setValorTotal(new java.math.BigDecimal("100.00"));

        PagamentoDebito debito = new PagamentoDebito();
        debito.setNumeroCartao("9876543210987654");
        debito.setReserva(reserva);

        // Act
        processador.processarERegistrarPagamento(reserva, debito);

        // Assert
        assertEquals(StatusPagamento.APROVADO, debito.getStatus());
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());
    }
}