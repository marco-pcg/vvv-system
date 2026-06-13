package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.enums.StatusOperacional;
import com.cefet.VVVSystem.domain.enums.TipoModal;

public record ModalResponseDTO(
        Long id,
        Long idTransportadora,
        String codigo,
        TipoModal tipo,
        Integer capacidade,
        StatusOperacional statusOperacional
) {
    public ModalResponseDTO(Modal modal) {
        this(
                modal.getId(),
                modal.getTransportadora() != null ? modal.getTransportadora().getId() : null,
                modal.getCodigo(),
                modal.getTipo(),
                modal.getCapacidade(),
                modal.getStatusOperacional()
        );
    }
}
