package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento_debito")
@PrimaryKeyJoinColumn(name = "id_pagamento")
public class PagamentoDebito extends Pagamento {
}
