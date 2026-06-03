package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "passageiro")
public class Passageiro {

    @Id
    @Column(length = 11, nullable = false)
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
}
