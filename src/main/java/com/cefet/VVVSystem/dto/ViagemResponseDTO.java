package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.StatusViagem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ViagemResponseDTO {
    private Long id;
    private Long idModal;
    private Long idCidadeOrigem;
    private Long idCidadeDestino;
    private LocalDateTime partida;
    private LocalDateTime chegada;
    private StatusViagem status;
    private BigDecimal preco;
}
