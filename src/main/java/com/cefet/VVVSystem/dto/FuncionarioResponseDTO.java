package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Funcionario;

import java.time.LocalDate;

public record FuncionarioResponseDTO(
        Long id,
        String cpf,
        String nome,
        String cep,
        LocalDate dataNascimento,
        String matricula,
        String email,
        String telefone,
        Long userId
) {
    public FuncionarioResponseDTO(Funcionario funcionario) {
        this(
                funcionario.getId(),
                funcionario.getCpf(),
                funcionario.getNome(),
                funcionario.getCep(),
                funcionario.getDataNascimento(),
                funcionario.getMatricula(),
                funcionario.getEmail(),
                funcionario.getTelefone(),
                funcionario.getUser() != null ? funcionario.getUser().getId() : null
        );
    }
}
