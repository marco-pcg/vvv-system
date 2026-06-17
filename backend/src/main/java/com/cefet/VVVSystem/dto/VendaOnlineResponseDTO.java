package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.enums.StatusReserva;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaOnlineResponseDTO {

    private Long reservaId;
    private String codigoReserva;
    private StatusReserva statusReserva;
    private String mensagem;

    public VendaOnlineResponseDTO(Long reservaId, String codigoReserva, StatusReserva statusReserva, String mensagem) {
        this.reservaId = reservaId;
        this.codigoReserva = codigoReserva;
        this.statusReserva = statusReserva;
        this.mensagem = mensagem;
    }
}
