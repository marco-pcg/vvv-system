package com.cefet.VVVSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AeroportoDTO(
        Long id,

        @NotBlank(message = "O código do aeroporto é obrigatório")
        @Size(min = 3, max = 3, message = "O código deve ter exatamente 3 letras")
        @Pattern(regexp = "^[A-Z]{3}$", message = "O código deve conter apenas 3 letras maiúsculas (padrão IATA)")
        String codigo,

        @NotBlank(message = "O nome do aeroporto é obrigatório")
        String nome,

        @NotNull(message = "O ID da cidade é obrigatório")
        Long cidadeId
) {}