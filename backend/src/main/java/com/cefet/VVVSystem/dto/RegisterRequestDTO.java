package com.cefet.VVVSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "O nome de usuário não pode ser vazio")
        @Size(min = 3, max = 100, message = "O nome de usuário deve ter entre 3 e 100 caracteres")
        String username,

        @NotBlank(message = "A senha não pode ser vazia")
        @Size(min = 4, message = "A senha deve ter no mínimo 4 caracteres")
        String password
) {
}
