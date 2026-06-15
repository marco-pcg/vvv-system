package com.cefet.VVVSystem.strategy;

import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.entity.PagamentoCredito;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;

public class PagamentoDebitoStrategy implements PagamentoStrategy {
    
   @Override
    public boolean processar(Pagamento pagamento) {
        PagamentoCredito credito = (PagamentoCredito) pagamento;
        // Implement specific credit card validation/processing logic here
        System.out.println("Processing Credit Card ending in: " + credito.getNumeroCartao());
        return true;
    }

    @Override
    public boolean seAplicaA(TipoPagamento tipoPagamento) {
        return "CREDITO".equalsIgnoreCase(tipoPagamento.toString());
    }
}
