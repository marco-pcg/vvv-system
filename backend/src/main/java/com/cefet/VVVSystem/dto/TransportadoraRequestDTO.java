package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record TransportadoraRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Schema(example = "Viação Cometa")
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório")
        @CNPJ(message = "O formato do CNPJ é inválido")
        @Schema(example = "12345678000199")
        String cnpj
) {
}
