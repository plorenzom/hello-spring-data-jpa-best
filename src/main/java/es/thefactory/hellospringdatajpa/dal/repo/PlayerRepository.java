package es.thefactory.hellospringdatajpa.dal.repo;

import es.thefactory.hellospringdatajpa.dal.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Pablo Lorenzo Manzano.
 */
@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {

    /**
     *
     * @param playerId
     * @return Número de registros eliminados (0 o 1).
     */
    int deleteByPlayerId(UUID playerId);

    /**
     *
     * @param teamId
     * @return boolean
     */
    boolean existsByTeamId(UUID teamId);
}
