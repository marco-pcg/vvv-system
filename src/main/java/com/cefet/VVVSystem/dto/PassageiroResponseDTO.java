package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Passageiro;

import java.time.LocalDate;

public record PassageiroResponseDTO(
        Long id,
        String cpf,
        String nome,
        String cep,
        LocalDate dataNascimento,
        String email,
        String telefone,
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
