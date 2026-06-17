package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record FuncionarioResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "52998224725")
        String cpf,
        @Schema(example = "João Silva")
        String nome,
        @Schema(example = "20040020")
        String cep,
        @Schema(example = "1990-05-15")
        LocalDate dataNascimento,
        @Schema(example = "MAT-001")
        String matricula,
        @Schema(example = "joao.silva@vvv.com.br")
        String email,
        @Schema(example = "21999998888")
        String telefone,
        @Schema(example = "1")
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
