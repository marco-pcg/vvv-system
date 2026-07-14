package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ViagemRequestDTO {

    @jakarta.validation.constraints.NotEmpty(message = "Pelo menos um Modal é obrigatório")
    @Schema(example = "[1, 2]")
    private java.util.List<Long> idsModais;

    @Schema(example = "[3]")
    private java.util.List<Long> idsEscalas;

    @NotNull(message = "ID da Cidade de Origem é obrigatório")
    @Schema(example = "1")
    private Long idCidadeOrigem;

    @NotNull(message = "ID da Cidade de Destino é obrigatório")
    @Schema(example = "2")
    private Long idCidadeDestino;

    @NotNull(message = "Data de partida é obrigatória")
    @Future(message = "Data de partida deve ser no futuro")
    @Schema(example = "2026-12-01T10:00:00")
    private LocalDateTime partida;

    @NotNull(message = "Data de chegada é obrigatória")
    @Future(message = "Data de chegada deve ser no futuro")
    @Schema(example = "2026-12-01T14:30:00")
    private LocalDateTime chegada;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    @Schema(example = "150.50")
    private BigDecimal preco;
}
