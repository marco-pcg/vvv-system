package com.cefet.VVVSystem.strategy;

import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.entity.PagamentoCredito;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;

import org.springframework.stereotype.Component;

@Component
public class PagamentoDebitoStrategy implements PagamentoStrategy {
    
    @Override
    public boolean processar(Pagamento pagamento) {
        com.cefet.VVVSystem.domain.entity.PagamentoDebito debito = (com.cefet.VVVSystem.domain.entity.PagamentoDebito) pagamento;
        System.out.println("Processing Debit Card ending in: " + debito.getNumeroCartao());
        return true;
    }

    @Override
    public boolean seAplicaA(TipoPagamento tipoPagamento) {
        return TipoPagamento.DEBITO.equals(tipoPagamento);
    }
}
