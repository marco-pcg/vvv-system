package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {

    /**
     * Verifica quantas viagens ativas existem usando os mesmos modais no mesmo intervalo de tempo.
     * Conflito ocorre quando a nova viagem começa antes da viagem existente terminar E a nova viagem termina depois da existente começar.
     */
    @Query("SELECT COUNT(v) FROM Viagem v JOIN v.modais m WHERE m.id IN :modalIds " +
           "AND v.partida < :chegada AND v.chegada > :partida")
    long countViagensConflitantes(@Param("modalIds") List<Long> modalIds, 
                                  @Param("partida") LocalDateTime partida, 
                                  @Param("chegada") LocalDateTime chegada);
}
