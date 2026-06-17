package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.StatusOperacional;
import com.cefet.VVVSystem.domain.enums.TipoModal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModalRequestDTO(
        @NotNull(message = "O ID da Transportadora é obrigatório")
        @Schema(example = "1")
        Long idTransportadora,

        @NotBlank(message = "O código é obrigatório")
        @Size(max = 20, message = "O código deve ter no máximo 20 caracteres")
        @Schema(example = "O-1002")
        String codigo,

        @NotNull(message = "O tipo de modal é obrigatório")
        @Schema(example = "ONIBUS")
        TipoModal tipo,

        @NotNull(message = "A capacidade é obrigatória")
        @Min(value = 1, message = "A capacidade deve ser no mínimo 1")
        @Schema(example = "42")
        Integer capacidade,

        @NotNull(message = "O status operacional é obrigatório")
        @Schema(example = "OPERACIONAL")
        StatusOperacional statusOperacional
) {
}
