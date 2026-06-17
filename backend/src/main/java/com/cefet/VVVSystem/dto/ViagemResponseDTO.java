package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.StatusViagem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ViagemResponseDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "[1, 2]")
    private java.util.List<Long> idsModais;
    @Schema(example = "[3]")
    private java.util.List<Long> idsEscalas;
    @Schema(example = "1")
    private Long idCidadeOrigem;
    @Schema(example = "2")
    private Long idCidadeDestino;
    @Schema(example = "2026-12-01T10:00:00")
    private LocalDateTime partida;
    @Schema(example = "2026-12-01T14:30:00")
    private LocalDateTime chegada;
    @Schema(example = "AGENDADA")
    private StatusViagem status;
    @Schema(example = "150.50")
    private BigDecimal preco;
}
