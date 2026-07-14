package com.cefet.VVVSystem.domain.entity;

import com.cefet.VVVSystem.domain.enums.TipoPagamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento_credito")
@PrimaryKeyJoinColumn(name = "id")
public class PagamentoCredito extends Pagamento {

    @Column(nullable = false)
    private Integer parcelas;

    @Column(nullable = false, length = 20)
    private String numeroCartao;
    
    @Override
    public TipoPagamento getTipo() {
        return TipoPagamento.CREDITO;
    }
}
