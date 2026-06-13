package com.cefet.VVVSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record FuncionarioRequestDTO(
        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        @Schema(example = "52998224725")
        String cpf,

        @NotBlank(message = "O nome é obrigatório")
        @Schema(example = "João Silva")
        String nome,

        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos")
        @Schema(example = "20040020")
        String cep,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Schema(example = "1990-05-15")
        LocalDate dataNascimento,

        @NotBlank(message = "A matrícula (código do funcionário) é obrigatória")
        @Schema(example = "MAT-001")
        String matricula,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail com formato inválido")
        @Schema(example = "joao.silva@vvv.com.br")
        String email,

        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
        @Schema(example = "21999998888")
        String telefone
) {
}
