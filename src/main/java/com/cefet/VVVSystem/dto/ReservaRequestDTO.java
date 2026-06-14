package com.cefet.VVVSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRequestDTO {

    @NotNull(message = "ID da Viagem é obrigatório")
    private Long idViagem;

    @NotNull(message = "ID do Cliente é obrigatório")
    private Long idCliente;

    @NotNull(message = "ID do Passageiro é obrigatório")
    private Long idPassageiro;
}
