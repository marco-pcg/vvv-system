package com.cefet.VVVSystem.strategy;

import org.springframework.stereotype.Component;

import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.entity.PagamentoDinheiro;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;

@Component
public class PagamentoDinheiroStrategy implements PagamentoStrategy {

    @Override
    public boolean processar(Pagamento pagamento) {
        PagamentoDinheiro dinheiro = (PagamentoDinheiro) pagamento;
        System.out.println("Processing Cash payment. Amount received: " + dinheiro.getValorRecebido() + " with Change: " + dinheiro.getTroco());
        // For cash, we assume the physical transaction was already successful at the counter.
        // The boolean return indicates the system successfully parsed and logged the transaction.
        return true;
    }

    @Override
    public boolean seAplicaA(TipoPagamento tipoPagamento) {
        return TipoPagamento.DINHEIRO.equals(tipoPagamento);
    }
}
