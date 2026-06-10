package com.cefet.VVVSystem.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import com.cefet.VVVSystem.domain.entity.FuncionarioPdv;
import com.cefet.VVVSystem.domain.entity.FuncionarioPdvId;
import com.cefet.VVVSystem.domain.entity.PontoDeVenda;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioPdvMappingTest {

    @Test
    @DisplayName("Should successfully persist and fetch the Many-to-Many entity relationship")
    void verifyFuncionarioPdvMapping() {
        Funcionario funcionario = new Funcionario();
        funcionario.setCpf("12345678901");
        funcionario.setNome("Mateus");
        funcionario.setEmail("mateus@cefet.br");

        PontoDeVenda pdv = new PontoDeVenda();
        pdv.setCnpj("12345678000199");
        pdv.setEndereco("Av. Maracanã, 229");

        // 2. Act: Set up the composite key join entity
        FuncionarioPdvId linkId = new FuncionarioPdvId(funcionario.getId(), pdv.getId());
        FuncionarioPdv relationship = new FuncionarioPdv();
        relationship.setId(linkId);
        relationship.setFuncionario(funcionario);
        relationship.setPdv(pdv);

        assertNotNull(relationship.getId());
        assertEquals("Mateus", relationship.getFuncionario().getNome());
        assertEquals("12345678000199", relationship.getPdv().getCnpj());
    }
}