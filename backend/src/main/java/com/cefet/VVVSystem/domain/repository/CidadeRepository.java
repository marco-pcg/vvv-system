package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Cidade;
import com.cefet.VVVSystem.domain.enums.UF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    Optional<Cidade> findByNomeAndUf(String nome, UF uf);
}
