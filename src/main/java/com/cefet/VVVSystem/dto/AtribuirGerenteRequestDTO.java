package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AtribuirGerenteRequestDTO(
        @NotNull(message = "O ID do gerente não pode ser nulo")
        @Schema(description = "ID do funcionário que será o gerente do PDV", example = "1")
        Long gerenteId
) {
}
