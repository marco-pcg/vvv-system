package com.cefet.VVVSystem.mapper;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Ticket;
import com.cefet.VVVSystem.domain.entity.Viagem;
import com.cefet.VVVSystem.dto.TicketResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDTO toResponseDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setNumero(ticket.getNumero());
        dto.setAssento(ticket.getAssento());

        Reserva reserva = ticket.getReserva();
        if (reserva != null) {
            dto.setIdReserva(reserva.getId());
            dto.setCodigoReserva(reserva.getCodigo());
            
            if (reserva.getPassageiro() != null) {
                dto.setPassageiroNome(reserva.getPassageiro().getNome());
                dto.setPassageiroCpf(reserva.getPassageiro().getCpf());
            }

            Viagem viagem = reserva.getViagem();
            if (viagem != null) {
                dto.setDataPartida(viagem.getPartida());
                dto.setDataChegada(viagem.getChegada());
                // Mapeamento simplificado das cidades se houver métodos na entidade Viagem, 
                // assumiremos null se não houver um método explícito (embora tenha relações de FK para cidade na viagem).
                // Evitamos carregar as entidades de cidade diretamente se não tivermos os getters simples.
            }
        }

        return dto;
    }
}
