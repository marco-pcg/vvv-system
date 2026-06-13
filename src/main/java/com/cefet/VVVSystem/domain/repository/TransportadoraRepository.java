package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Transportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {
}
