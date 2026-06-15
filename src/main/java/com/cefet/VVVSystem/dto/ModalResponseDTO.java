package com.cefet.VVVSystem.dto;

import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.enums.StatusOperacional;
import com.cefet.VVVSystem.domain.enums.TipoModal;
import io.swagger.v3.oas.annotations.media.Schema;

public record ModalResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "1")
        Long idTransportadora,
        @Schema(example = "O-1002")
        String codigo,
        @Schema(example = "ONIBUS")
        TipoModal tipo,
        @Schema(example = "42")
        Integer capacidade,
        @Schema(example = "OPERACIONAL")
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
