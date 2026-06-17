package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PassageiroRequestDTO(
        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        @Schema(example = "98765432100")
        String cpf,

        @NotBlank(message = "O nome é obrigatório")
        @Schema(example = "Maria Oliveira")
        String nome,

        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos")
        @Schema(example = "30130000")
        String cep,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Schema(example = "1985-08-20")
        LocalDate dataNascimento,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail com formato inválido")
        @Schema(example = "maria.oliveira@email.com")
        String email,

        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
        @Schema(example = "31988887777")
        String telefone,

        @NotNull(message = "O campo possui acompanhante é obrigatório")
        @Schema(example = "false")
        Boolean possuiAcompanhante
) {
}
