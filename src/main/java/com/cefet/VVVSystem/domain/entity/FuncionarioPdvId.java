package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FuncionarioPdvId implements Serializable {

    @Column(name = "funcionario_id")
    private Long funcionarioId;

    @Column(name = "pdv_id")
    private Long pdvId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuncionarioPdvId that = (FuncionarioPdvId) o;
        return Objects.equals(funcionarioId, that.funcionarioId)
                && Objects.equals(pdvId, that.pdvId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcionarioId, pdvId);
    }
}
