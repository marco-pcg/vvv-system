package com.cefet.VVVSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AlterarSenhaRequestDTO {
    @NotBlank
    private String senhaAtual;
    @NotBlank
    private String novaSenha;
}
