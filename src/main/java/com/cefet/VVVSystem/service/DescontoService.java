package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Passageiro;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

/**
 * Serviço responsável pelo cálculo de desconto para passageiros menores de idade (RI03/RF15).
 *
 * Regras:
 * - Crianças de 0 a 5 anos: passagem gratuita (100% de desconto),
 *   desde que acompanhadas (possuiAcompanhante = true).
 * - Crianças de 6 a 11 anos: 50% de desconto.
 * - Adolescentes de 12 a 17 anos: 25% de desconto.
 * - Adultos (18+ anos): sem desconto.
 */
@Service
public class DescontoService {

    private static final BigDecimal DESCONTO_CRIANCA_0_5 = new BigDecimal("1.00");   // 100%
    private static final BigDecimal DESCONTO_CRIANCA_6_11 = new BigDecimal("0.50");  // 50%
    private static final BigDecimal DESCONTO_ADOLESCENTE = new BigDecimal("0.25");   // 25%
    private static final BigDecimal SEM_DESCONTO = BigDecimal.ZERO;

    /**
     * Calcula o valor final da passagem com base na idade do passageiro
     * na data da viagem.
     *
     * @param precoOriginal preço original da viagem
     * @param passageiro    o passageiro que fará a viagem
     * @param dataViagem    data da partida da viagem (usada para calcular a idade)
     * @return o valor final com o desconto aplicado
     */
    public BigDecimal calcularPrecoComDesconto(BigDecimal precoOriginal, Passageiro passageiro, LocalDate dataViagem) {
        BigDecimal percentualDesconto = calcularPercentualDesconto(passageiro, dataViagem);
        BigDecimal valorDesconto = precoOriginal.multiply(percentualDesconto);
        return precoOriginal.subtract(valorDesconto).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Retorna o percentual de desconto aplicável ao passageiro.
     *
     * @param passageiro o passageiro
     * @param dataReferencia data de referência para calcular a idade (normalmente a data da viagem)
     * @return percentual de desconto (ex: 0.50 para 50%)
     */
    public BigDecimal calcularPercentualDesconto(Passageiro passageiro, LocalDate dataReferencia) {
        if (passageiro.getDataNascimento() == null) {
            return SEM_DESCONTO;
        }

        int idade = calcularIdade(passageiro.getDataNascimento(), dataReferencia);

        if (idade >= 0 && idade <= 5) {
            if (Boolean.TRUE.equals(passageiro.getPossuiAcompanhante())) {
                return DESCONTO_CRIANCA_0_5;
            }
            // Criança de 0-5 sem acompanhante: aplica desconto de 50% (mesma faixa de 6-11)
            return DESCONTO_CRIANCA_6_11;
        }

        if (idade >= 6 && idade <= 11) {
            return DESCONTO_CRIANCA_6_11;
        }

        if (idade >= 12 && idade <= 17) {
            return DESCONTO_ADOLESCENTE;
        }

        return SEM_DESCONTO;
    }

    /**
     * Calcula a idade do passageiro em uma data de referência.
     */
    private int calcularIdade(LocalDate dataNascimento, LocalDate dataReferencia) {
        return Period.between(dataNascimento, dataReferencia).getYears();
    }
}
