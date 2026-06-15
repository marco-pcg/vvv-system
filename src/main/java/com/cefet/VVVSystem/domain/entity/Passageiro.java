package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "passageiro")
public class Passageiro extends Pessoa {

    @Column(name = "possui_acompanhante", nullable = false)
    private Boolean possuiAcompanhante = false;
}
