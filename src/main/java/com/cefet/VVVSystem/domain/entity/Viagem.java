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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modal", nullable = false)
    private Modal modal;

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
