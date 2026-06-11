package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "passageiro")
public class Passageiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(length = 8)
    private String cep;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 11)
    private String telefone;

    @Column(name = "possui_acompanhante", nullable = false)
    private Boolean possuiAcompanhante = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passageiro that = (Passageiro) o;
        return cpf != null && Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
