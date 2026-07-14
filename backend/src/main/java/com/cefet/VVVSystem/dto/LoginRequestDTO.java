package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "O nome de usuário não pode estar em branco")
    @Schema(example = "admin")
    String username,
    
    @NotBlank(message = "A senha não pode estar em branco")
    @Schema(example = "admin123")
    String password
) {}