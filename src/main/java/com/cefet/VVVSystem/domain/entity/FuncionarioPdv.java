package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "funcionario_pdv")
public class FuncionarioPdv {

    @EmbeddedId
    private FuncionarioPdvId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("funcionarioId")
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pdvId")
    @JoinColumn(name = "pdv_id")
    private PontoDeVenda pdv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuncionarioPdv that = (FuncionarioPdv) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
