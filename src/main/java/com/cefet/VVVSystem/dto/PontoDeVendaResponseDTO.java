package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.PontoDeVenda;

public record PontoDeVendaResponseDTO(
        Long id,
        String cnpj,
        String endereco,
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
