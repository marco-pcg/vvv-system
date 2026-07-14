package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.UF;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CidadeRequestDTO(
        @NotBlank(message = "O nome da cidade é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        @Schema(example = "Belo Horizonte")
        String nome,
        
        @NotNull(message = "A UF é obrigatória")
        @Schema(example = "MG")
        UF uf
) {}
