package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento_credito")
@PrimaryKeyJoinColumn(name = "id_pagamento")
public class PagamentoCredito extends Pagamento {

    @Column(nullable = false)
    private Integer parcelas;
}
