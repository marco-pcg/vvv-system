package com.cefet.VVVSystem.mapper;

import com.cefet.VVVSystem.domain.entity.Cidade;
import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.entity.Viagem;
import com.cefet.VVVSystem.dto.ViagemRequestDTO;
import com.cefet.VVVSystem.dto.ViagemResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ViagemMapper {

    public Viagem toEntity(ViagemRequestDTO dto, java.util.Set<Modal> modais, Cidade cidadeOrigem, Cidade cidadeDestino, java.util.Set<Cidade> escalas) {
        if (dto == null) {
            return null;
        }

        Viagem viagem = new Viagem();
        viagem.setModais(modais);
        viagem.setCidadeOrigem(cidadeOrigem);
        viagem.setCidadeDestino(cidadeDestino);
        if (escalas != null) {
            viagem.setEscalas(escalas);
        }
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
        if (viagem.getModais() != null) {
            dto.setIdsModais(viagem.getModais().stream().map(Modal::getId).collect(java.util.stream.Collectors.toList()));
        }
        if (viagem.getEscalas() != null) {
            dto.setIdsEscalas(viagem.getEscalas().stream().map(Cidade::getId).collect(java.util.stream.Collectors.toList()));
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
