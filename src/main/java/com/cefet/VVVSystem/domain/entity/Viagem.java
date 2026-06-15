package com.cefet.VVVSystem.domain.entity;

import com.cefet.VVVSystem.domain.enums.StatusViagem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "viagem")
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "viagem_modal",
        joinColumns = @JoinColumn(name = "viagem_id"),
        inverseJoinColumns = @JoinColumn(name = "modal_id")
    )
    private java.util.Set<Modal> modais = new java.util.HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "viagem_escala",
        joinColumns = @JoinColumn(name = "viagem_id"),
        inverseJoinColumns = @JoinColumn(name = "cidade_id")
    )
    private java.util.Set<Cidade> escalas = new java.util.HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_origem", nullable = false)
    private Cidade cidadeOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cidade_destino", nullable = false)
    private Cidade cidadeDestino;

    @Column(nullable = false)
    private LocalDateTime partida;

    @Column(nullable = false)
    private LocalDateTime chegada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusViagem status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viagem viagem = (Viagem) o;
        return id != null && Objects.equals(id, viagem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
