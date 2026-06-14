package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.StatusReserva;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaResponseDTO {
    private Long id;
    private String codigo;
    private Long idViagem;
    private Long idCliente;
    private Long idPassageiro;
    private LocalDateTime dataCriacao;
    private StatusReserva status;
    private BigDecimal valorTotal;
}
