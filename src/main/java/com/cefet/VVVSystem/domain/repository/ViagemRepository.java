package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {
}
