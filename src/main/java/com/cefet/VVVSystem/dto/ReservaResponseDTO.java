package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.StatusReserva;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaResponseDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "RES-123456")
    private String codigo;
    @Schema(example = "1")
    private Long idViagem;
    @Schema(example = "2")
    private Long idCliente;
    @Schema(example = "3")
    private Long idPassageiro;
    @Schema(example = "2026-11-20T10:00:00")
    private LocalDateTime dataCriacao;
    @Schema(example = "CONFIRMADA")
    private StatusReserva status;
    @Schema(example = "150.50")
    private BigDecimal valorTotal;
}
