package com.cefet.VVVSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.enums.StatusPagamento;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import com.cefet.VVVSystem.strategy.PagamentoStrategy;

@Service
public class ProcessadorPagamento {

    // Spring auto-injects all components implementing EstrategiaPagamento here!
    private final List<PagamentoStrategy> estrategias;

    public ProcessadorPagamento(List<PagamentoStrategy> estrategias) {
        this.estrategias = estrategias;
    }

    public void processarERegistrarPagamento(Reserva reserva, Pagamento pagamento) {
        // Convert entity string suffix to your matching TipoPagamento enum instance
        String suffix = pagamento.getClass().getSimpleName()
                                .replace("Pagamento", "")
                                .toUpperCase();
        TipoPagamento tipoMapping = TipoPagamento.valueOf(suffix);

        // 1. Automatic Strategy Selection (Fixed the Stream pipeline assignment)
        PagamentoStrategy estrategiaSelecionada = estrategias.stream()
                .filter(e -> e.seAplicaA(tipoMapping)) // <-- Added missing closing parenthesis here
                .findFirst()                          // <-- Plucks the single match from the stream
                .orElseThrow(() -> new IllegalArgumentException("No payment strategy found for type: " + tipoMapping));
        // 2. Execute Payment Transaction Logic
        boolean sucesso = estrategiaSelecionada.processar(pagamento);

        if (sucesso) {
            // 3. Registrar Pagamento (Link transaction meta to entity)
            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setReserva(reserva); // FK lives on Pagamento side (pagamento.id_reserva)

            // 4. Atualizar status da reserva
            reserva.setStatus(StatusReserva.CONFIRMADA); // Updates your reservation state machine
            System.out.println("Reservation " + reserva.getId() + " successfully confirmed via Strategy!");
        } else {
            pagamento.setStatus(StatusPagamento.RECUSADO);
            reserva.setStatus(StatusReserva.PENDENTE);
        }
    }
}