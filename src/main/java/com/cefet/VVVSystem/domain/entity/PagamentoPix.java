package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento_pix")
@PrimaryKeyJoinColumn(name = "id")
public class PagamentoPix extends Pagamento {

    @Column(name = "chave_pix", nullable = false, length = 60)
    private String chavePix;
}
