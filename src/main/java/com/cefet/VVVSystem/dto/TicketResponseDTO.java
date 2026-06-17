package com.cefet.VVVSystem.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {
    private Long id;
    private String numero;
    private String assento;
    private Long idReserva;
    private String codigoReserva;
    private String passageiroNome;
    private String passageiroCpf;
    private String origemCidade;
    private String destinoCidade;
    private LocalDateTime dataPartida;
    private LocalDateTime dataChegada;
}
