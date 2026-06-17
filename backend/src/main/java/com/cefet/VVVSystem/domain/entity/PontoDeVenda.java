package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ponto_de_venda")
public class PontoDeVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(nullable = false)
    private String endereco;

    /**
     * RN18: Cada PDV possui exatamente um gerente responsável.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gerente_id", nullable = false)
    private Funcionario gerente;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PontoDeVenda that = (PontoDeVenda) o;
        return cnpj != null && Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnpj);
    }
}
