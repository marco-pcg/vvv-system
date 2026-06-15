package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Transportadora;
import io.swagger.v3.oas.annotations.media.Schema;

public record TransportadoraResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Viação Cometa")
        String nome,
        @Schema(example = "12345678000199")
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
