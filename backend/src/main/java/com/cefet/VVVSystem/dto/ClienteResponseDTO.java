package com.cefet.VVVSystem.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String cpf;
    private String nome;
    private String cep;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
}
