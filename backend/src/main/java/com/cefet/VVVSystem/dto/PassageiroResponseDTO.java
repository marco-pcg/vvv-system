package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Passageiro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PassageiroResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "98765432100")
        String cpf,
        @Schema(example = "Maria Oliveira")
        String nome,
        @Schema(example = "30130000")
        String cep,
        @Schema(example = "1985-08-20")
        LocalDate dataNascimento,
        @Schema(example = "maria.oliveira@email.com")
        String email,
        @Schema(example = "31988887777")
        String telefone,
        @Schema(example = "false")
        Boolean possuiAcompanhante
) {
    public PassageiroResponseDTO(Passageiro passageiro) {
        this(
                passageiro.getId(),
                passageiro.getCpf(),
                passageiro.getNome(),
                passageiro.getCep(),
                passageiro.getDataNascimento(),
                passageiro.getEmail(),
                passageiro.getTelefone(),
                passageiro.getPossuiAcompanhante()
        );
    }
}
