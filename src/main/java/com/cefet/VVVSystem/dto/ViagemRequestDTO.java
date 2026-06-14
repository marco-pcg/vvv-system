package com.cefet.VVVSystem.dto;

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

    @NotNull(message = "ID do Modal é obrigatório")
    private Long idModal;

    @NotNull(message = "ID da Cidade de Origem é obrigatório")
    private Long idCidadeOrigem;

    @NotNull(message = "ID da Cidade de Destino é obrigatório")
    private Long idCidadeDestino;

    @NotNull(message = "Data de partida é obrigatória")
    @Future(message = "Data de partida deve ser no futuro")
    private LocalDateTime partida;

    @NotNull(message = "Data de chegada é obrigatória")
    @Future(message = "Data de chegada deve ser no futuro")
    private LocalDateTime chegada;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    private BigDecimal preco;
}
