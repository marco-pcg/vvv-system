package com.cefet.VVVSystem.dto;

import jakarta.validation.constraints.NotNull;

public record AtribuirGerenteRequestDTO(
        @NotNull(message = "O ID do gerente não pode ser nulo")
        Long gerenteId
) {
}
