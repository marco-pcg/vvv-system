package com.cefet.VVVSystem.domain.entity;

import com.cefet.VVVSystem.domain.enums.UF;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "cidade", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cidade_nome_uf", columnNames = {"nome", "uf"})
})
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private UF uf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return nome != null && uf != null
                && Objects.equals(nome, cidade.nome)
                && Objects.equals(uf, cidade.uf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, uf);
    }
}
