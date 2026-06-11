package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.UF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CidadeDTO(
         Long id,
        
        @NotBlank(message = "O nome da cidade é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,
        
        @NotNull(message = "A UF é obrigatória")
        UF uf
) {}
