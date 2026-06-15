package com.cefet.VVVSystem.strategy;

import com.cefet.VVVSystem.strategy.PagamentoStrategy;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.entity.PagamentoPix;

import org.springframework.stereotype.Component;

@Component
public class PagamentoPixStrategy implements PagamentoStrategy {
    
    @Override
    public boolean processar(Pagamento pagamento) {
        PagamentoPix pix = (PagamentoPix) pagamento;
        System.out.println("Processing Pix payment to: " + pix.getChavePix());
        return true;
    }

    @Override
    public boolean seAplicaA(TipoPagamento tipoPagamento) {
        return "PIX".equalsIgnoreCase(tipoPagamento.toString());
    }

}
