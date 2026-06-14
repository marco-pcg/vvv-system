package com.cefet.VVVSystem.mapper;

import com.cefet.VVVSystem.domain.entity.Cidade;
import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.entity.Viagem;
import com.cefet.VVVSystem.dto.ViagemRequestDTO;
import com.cefet.VVVSystem.dto.ViagemResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ViagemMapper {

    public Viagem toEntity(ViagemRequestDTO dto, Modal modal, Cidade cidadeOrigem, Cidade cidadeDestino) {
        if (dto == null) {
            return null;
        }

        Viagem viagem = new Viagem();
        viagem.setModal(modal);
        viagem.setCidadeOrigem(cidadeOrigem);
        viagem.setCidadeDestino(cidadeDestino);
        viagem.setPartida(dto.getPartida());
        viagem.setChegada(dto.getChegada());
        viagem.setPreco(dto.getPreco());

        return viagem;
    }

    public ViagemResponseDTO toResponseDTO(Viagem viagem) {
        if (viagem == null) {
            return null;
        }

        ViagemResponseDTO dto = new ViagemResponseDTO();
        dto.setId(viagem.getId());
        if (viagem.getModal() != null) {
            dto.setIdModal(viagem.getModal().getId());
        }
        if (viagem.getCidadeOrigem() != null) {
            dto.setIdCidadeOrigem(viagem.getCidadeOrigem().getId());
        }
        if (viagem.getCidadeDestino() != null) {
            dto.setIdCidadeDestino(viagem.getCidadeDestino().getId());
        }
        dto.setPartida(viagem.getPartida());
        dto.setChegada(viagem.getChegada());
        dto.setStatus(viagem.getStatus());
        dto.setPreco(viagem.getPreco());

        return dto;
    }
}
