package com.cefet.VVVSystem.domain.entity;

import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento_dinheiro")
public class PagamentoDinheiro extends Pagamento {

    @Column(name = "valor_recebido", nullable = false)
    private Double valorRecebido;

    @Column(nullable = false)
    private Double troco;

    public PagamentoDinheiro() {
        this.setTipo(TipoPagamento.DINHEIRO);
    }
}
