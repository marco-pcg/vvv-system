package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Transportadora;

public record TransportadoraResponseDTO(
        Long id,
        String nome,
        String cnpj
) {
    public TransportadoraResponseDTO(Transportadora transportadora) {
        this(
                transportadora.getId(),
                transportadora.getNome(),
                transportadora.getCnpj()
        );
    }
}
