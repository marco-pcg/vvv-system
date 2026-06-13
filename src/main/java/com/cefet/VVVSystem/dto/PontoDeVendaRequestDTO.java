package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

public record PontoDeVendaRequestDTO(
        @NotBlank(message = "O CNPJ é obrigatório")
        @CNPJ(message = "CNPJ inválido")
        @Schema(example = "12345678000199")
        String cnpj,

        @NotBlank(message = "O endereço é obrigatório")
        @Schema(example = "Av. Maracanã, 229")
        String endereco,

        @NotNull(message = "O ID do gerente responsável é obrigatório")
        @Schema(example = "1")
        Long gerenteId
) {
}
