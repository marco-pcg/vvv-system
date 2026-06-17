package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByMatricula(String matricula);
    
    @Query("SELECT f FROM Funcionario f WHERE f.pessoa.cpf = :cpf")
    Optional<Funcionario> findByPessoaCpf(@Param("cpf") String cpf);
}
