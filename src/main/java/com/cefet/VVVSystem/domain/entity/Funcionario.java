package com.cefet.VVVSystem.domain.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.cefet.VVVSystem.exception.BusinessException;

import com.cefet.VVVSystem.domain.enums.StatusOperacional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "funcionario")
public class Funcionario {

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

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 11)
    private String telefone;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return cpf != null && Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    public void colocarModalEmManutencao(Modal modal) {
        if (modal == null) {
            throw new IllegalArgumentException("Modal cannot be null");
        }

        modal.setStatusOperacional(StatusOperacional.EM_MANUTENCAO);
    }
}
