package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AlocarFuncionarioRequestDTO(
        @NotNull(message = "O ID do Ponto de Venda não pode ser nulo")
        @Schema(description = "ID do Ponto de Venda onde o funcionário será alocado", example = "1")
        Long pdvId
) {
}
