package com.cefet.VVVSystem.strategy;

import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.entity.PagamentoCredito;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class PagamentoCreditoStrategy implements PagamentoStrategy {
    
    @Override
    public boolean processar(Pagamento pagamento) {
        PagamentoCredito credito = (PagamentoCredito) pagamento;
        
        if (credito.getReserva() != null && credito.getReserva().getValorTotal() != null) {
            BigDecimal valorAtual = credito.getReserva().getValorTotal();
            
            // RN15: Acima de 4 parcelas aplica 5% de juros
            if (credito.getParcelas() != null && credito.getParcelas() > 4) {
                BigDecimal juros = valorAtual.multiply(new BigDecimal("0.05"));
                BigDecimal valorComJuros = valorAtual.add(juros).setScale(2, RoundingMode.HALF_UP);
                credito.getReserva().setValorTotal(valorComJuros);
            }
        }

        System.out.println("Processing Credit Card ending in: " + credito.getNumeroCartao() + " with " + credito.getParcelas() + " installments.");
        return true;
    }

    @Override
    public boolean seAplicaA(TipoPagamento tipoPagamento) {
        return TipoPagamento.CREDITO.equals(tipoPagamento);
    }
}
