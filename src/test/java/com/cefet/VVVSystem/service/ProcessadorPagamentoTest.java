package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.PagamentoPix;
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

    @BeforeEach
    void setUp() {
        // Instantiate the strategy list manually for a pure lightweight unit test
        List<PagamentoStrategy> estrategias = List.of(
            new PagamentoPixStrategy(),
            new PagamentoDebitoStrategy(),
            new PagamentoCreditoStrategy()
        );
        processador = new ProcessadorPagamento(estrategias);
    }

    @Test
    @DisplayName("Should automatically select Pix strategy and confirm reservation status")
    void testProcessamentoComSucessoPix() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId(42L);
        reserva.setStatus(StatusReserva.PENDENTE);

        PagamentoPix pix = new PagamentoPix();
        pix.setChavePix("pix@cefet.br");
        pix.setValor(250.00);

        // Act
        processador.processarERegistrarPagamento(reserva, pix);

        // Assert
        assertEquals(StatusPagamento.APROVADO, pix.getStatus(), "Payment status should transition to CONFIRMADO.");
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus(), "Reservation should be CONFIRMADA.");
        assertNotNull(pix.getReserva(), "Payment record should be successfully bound to the reservation.");
    }
}