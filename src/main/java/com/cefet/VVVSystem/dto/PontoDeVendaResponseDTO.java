package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.PontoDeVenda;
import io.swagger.v3.oas.annotations.media.Schema;

public record PontoDeVendaResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "06990590000123")
        String cnpj,
        @Schema(example = "Av. Maracanã, 229")
        String endereco,
        @Schema(example = "1")
        Long gerenteId
) {
    public PontoDeVendaResponseDTO(PontoDeVenda pdv) {
        this(
                pdv.getId(),
                pdv.getCnpj(),
                pdv.getEndereco(),
                pdv.getGerente() != null ? pdv.getGerente().getId() : null
        );
    }
}
