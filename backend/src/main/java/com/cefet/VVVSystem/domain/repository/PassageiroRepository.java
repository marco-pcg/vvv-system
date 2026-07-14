package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Passageiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PassageiroRepository extends JpaRepository<Passageiro, Long> {
    @Query("SELECT p FROM Passageiro p WHERE p.pessoa.cpf = :cpf")
    Optional<Passageiro> findByPessoaCpf(@Param("cpf") String cpf);
}
