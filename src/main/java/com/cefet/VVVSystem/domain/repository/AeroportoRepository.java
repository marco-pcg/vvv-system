package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {
    Optional<Aeroporto> findByCodigo(String codigo);
}