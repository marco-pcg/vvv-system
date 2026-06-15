package com.cefet.VVVSystem.domain.entity;

import java.util.HashSet;
import java.util.Set;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.domain.enums.StatusOperacional;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "funcionario")
public class Funcionario extends Pessoa {

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "funcionario_pdv",
            joinColumns = @JoinColumn(name = "funcionario_id"),
            inverseJoinColumns = @JoinColumn(name = "pdv_id")
    )
    private Set<PontoDeVenda> pontosDeVenda = new HashSet<>();

    public void autorizarEmPontoDeVenda(PontoDeVenda pdv) {
        if (pdv == null) {
            throw new IllegalArgumentException("Ponto de Venda inválido");
        }
        if (this.pontosDeVenda.size() >= 2 && !this.pontosDeVenda.contains(pdv)) {
            throw new BusinessException("Limite excedido: Um funcionário pode atuar em no máximo 2 Pontos de Venda.");
        }
        this.pontosDeVenda.add(pdv);
    }

    public void colocarModalEmManutencao(Modal modal) {
        if (modal == null) {
            throw new IllegalArgumentException("Modal cannot be null");
        }
        modal.setStatusOperacional(StatusOperacional.EM_MANUTENCAO);
    }
}
