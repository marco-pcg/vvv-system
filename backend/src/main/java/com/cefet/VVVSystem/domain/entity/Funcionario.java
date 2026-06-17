package com.cefet.VVVSystem.domain.entity;

import java.util.HashSet;
import java.util.Set;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.domain.enums.StatusOperacional;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa = new Pessoa();

    // Delegates para manter a compatibilidade
    public String getCpf() { return pessoa != null ? pessoa.getCpf() : null; }
    public void setCpf(String cpf) { if(pessoa != null) pessoa.setCpf(cpf); }
    public String getNome() { return pessoa != null ? pessoa.getNome() : null; }
    public void setNome(String nome) { if(pessoa != null) pessoa.setNome(nome); }
    public String getCep() { return pessoa != null ? pessoa.getCep() : null; }
    public void setCep(String cep) { if(pessoa != null) pessoa.setCep(cep); }
    public LocalDate getDataNascimento() { return pessoa != null ? pessoa.getDataNascimento() : null; }
    public void setDataNascimento(LocalDate dataNascimento) { if(pessoa != null) pessoa.setDataNascimento(dataNascimento); }
    public String getEmail() { return pessoa != null ? pessoa.getEmail() : null; }
    public void setEmail(String email) { if(pessoa != null) pessoa.setEmail(email); }
    public String getTelefone() { return pessoa != null ? pessoa.getTelefone() : null; }
    public void setTelefone(String telefone) { if(pessoa != null) pessoa.setTelefone(telefone); }

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
