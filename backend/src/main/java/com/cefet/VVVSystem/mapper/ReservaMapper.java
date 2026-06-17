package com.cefet.VVVSystem.mapper;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.dto.ReservaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaResponseDTO toResponseDTO(Reserva reserva) {
        if (reserva == null) {
            return null;
        }

        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(reserva.getId());
        dto.setCodigo(reserva.getCodigo());
        if (reserva.getViagem() != null) {
            dto.setIdViagem(reserva.getViagem().getId());
        }
        if (reserva.getCliente() != null) {
            dto.setIdCliente(reserva.getCliente().getId());
        }
        if (reserva.getPassageiro() != null) {
            dto.setIdPassageiro(reserva.getPassageiro().getId());
        }
        dto.setDataCriacao(reserva.getDataCriacao());
        dto.setStatus(reserva.getStatus());
        dto.setValorTotal(reserva.getValorTotal());

        return dto;
    }
}
