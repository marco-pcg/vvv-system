package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRequestDTO {

    @NotNull(message = "ID da Viagem é obrigatório")
    @Schema(example = "1")
    private Long idViagem;

    @NotNull(message = "ID do Cliente é obrigatório")
    @Schema(example = "2")
    private Long idCliente;

    @NotNull(message = "ID do Passageiro é obrigatório")
    @Schema(example = "3")
    private Long idPassageiro;
}
