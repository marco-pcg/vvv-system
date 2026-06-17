package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record PagamentoFisicoRequestDTO(
    @NotNull(message = "O ID da reserva é obrigatório")
    @Schema(description = "ID da reserva pendente", example = "2")
    Long idReserva,

    @NotNull(message = "O tipo de pagamento é obrigatório")
    @Schema(description = "Tipo do pagamento: CREDITO, DEBITO ou DINHEIRO", example = "DINHEIRO")
    TipoPagamento tipoPagamento,

    @Schema(description = "Número do cartão (se aplicável)", example = "1234567812345678")
    String numeroCartao,

    @Schema(description = "Quantidade de parcelas (para crédito)", example = "1")
    Integer parcelas,

    @Schema(description = "Valor recebido em dinheiro (para cálculo de troco)", example = "200.00")
    Double valorRecebido
) {}
