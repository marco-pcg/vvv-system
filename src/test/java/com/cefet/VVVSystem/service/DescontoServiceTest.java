package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Passageiro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DescontoServiceTest {

    private DescontoService descontoService;

    @BeforeEach
    void setUp() {
        descontoService = new DescontoService();
    }

    private Passageiro criarPassageiro(LocalDate dataNascimento, boolean possuiAcompanhante) {
        Passageiro p = new Passageiro();
        p.setDataNascimento(dataNascimento);
        p.setPossuiAcompanhante(possuiAcompanhante);
        return p;
    }

    // --- Testes para crianças de 0 a 5 anos ---

    @Test
    void crianca0a5_comAcompanhante_deveSerGratuito() {
        LocalDate dataViagem = LocalDate.of(2026, 7, 1);
        Passageiro passageiro = criarPassageiro(LocalDate.of(2023, 1, 1), true); // 3 anos
        BigDecimal preco = new BigDecimal("200.00");

        BigDecimal resultado = descontoService.calcularPrecoComDesconto(preco, passageiro, dataViagem);

        assertEquals(new BigDecimal("0.00"), resultado);
    }

    @Test
    void crianca0a5_semAcompanhante_deveReceber50Porcento() {
        LocalDate dataViagem = LocalDate.of(2026, 7, 1);
        Passageiro passageiro = criarPassageiro(LocalDate.of(2022, 1, 1), false); // 4 anos
        BigDecimal preco = new BigDecimal("200.00");

        BigDecimal resultado = descontoService.calcularPrecoComDesconto(preco, passageiro, dataViagem);

        assertEquals(new BigDecimal("100.00"), resultado);
    }

    // --- Testes para crianças de 6 a 11 anos ---

    @Test
    void crianca6a11_deveReceber50Porcento() {
        LocalDate dataViagem = LocalDate.of(2026, 7, 1);
        Passageiro passageiro = criarPassageiro(LocalDate.of(2018, 1, 1), false); // 8 anos
        BigDecimal preco = new BigDecimal("300.00");

        BigDecimal resultado = descontoService.calcularPrecoComDesconto(preco, passageiro, dataViagem);

        assertEquals(new BigDecimal("150.00"), resultado);
    }

    // --- Testes para adolescentes de 12 a 17 anos ---

    @Test
    void adolescente12a17_deveReceber25Porcento() {
        LocalDate dataViagem = LocalDate.of(2026, 7, 1);
        Passageiro passageiro = criarPassageiro(LocalDate.of(2011, 1, 1), false); // 15 anos
        BigDecimal preco = new BigDecimal("400.00");

        BigDecimal resultado = descontoService.calcularPrecoComDesconto(preco, passageiro, dataViagem);

        assertEquals(new BigDecimal("300.00"), resultado);
    }

    // --- Testes para adultos (18+) ---

    @Test
    void adulto_naoDeveReceberDesconto() {
        LocalDate dataViagem = LocalDate.of(2026, 7, 1);
        Passageiro passageiro = criarPassageiro(LocalDate.of(2000, 1, 1), false); // 26 anos
        BigDecimal preco = new BigDecimal("500.00");

        BigDecimal resultado = descontoService.calcularPrecoComDesconto(preco, passageiro, dataViagem);

        assertEquals(new BigDecimal("500.00"), resultado);
    }

    // --- Teste sem data de nascimento ---

    @Test
    void semDataNascimento_naoDeveReceberDesconto() {
        LocalDate dataViagem = LocalDate.of(2026, 7, 1);
        Passageiro passageiro = criarPassageiro(null, false);
        BigDecimal preco = new BigDecimal("250.00");

        BigDecimal resultado = descontoService.calcularPrecoComDesconto(preco, passageiro, dataViagem);

        assertEquals(new BigDecimal("250.00"), resultado);
    }

    // --- Teste de percentual ---

    @Test
    void percentual_crianca6a11_deve_ser_50Porcento() {
        LocalDate dataViagem = LocalDate.of(2026, 7, 1);
        Passageiro passageiro = criarPassageiro(LocalDate.of(2016, 1, 1), false); // 10 anos

        BigDecimal percentual = descontoService.calcularPercentualDesconto(passageiro, dataViagem);

        assertEquals(new BigDecimal("0.50"), percentual);
    }
}
