package com.cefet.VVVSystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa = new Pessoa();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

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
}
