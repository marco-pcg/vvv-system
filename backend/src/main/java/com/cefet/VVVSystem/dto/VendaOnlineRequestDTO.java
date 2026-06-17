package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaOnlineRequestDTO {

    @NotNull(message = "Reserva Request DTO é obrigatório")
    private ReservaRequestDTO reserva;

    @NotNull(message = "Tipo de pagamento é obrigatório")
    private TipoPagamento tipoPagamento;

    @NotBlank(message = "Número do cartão é obrigatório")
    private String numeroCartao;

    @NotNull(message = "Quantidade de parcelas é obrigatória")
    private Integer parcelas;
}
