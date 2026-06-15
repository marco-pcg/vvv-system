package com.cefet.VVVSystem.strategy;

import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;

public interface PagamentoStrategy {
    
    boolean processar(Pagamento pagamento);

    boolean seAplicaA(TipoPagamento tipoPagamento);

}
