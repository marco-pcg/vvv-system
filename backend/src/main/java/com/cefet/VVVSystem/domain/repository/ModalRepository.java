package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Modal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModalRepository extends JpaRepository<Modal, Long> {
    Optional<Modal> findByCodigo(String codigo);
}
