package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Passageiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassageiroRepository extends JpaRepository<Passageiro, Long> {
}
