package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUserId(Long userId);
    Optional<Cliente> findByPessoaEmail(String email);
}
