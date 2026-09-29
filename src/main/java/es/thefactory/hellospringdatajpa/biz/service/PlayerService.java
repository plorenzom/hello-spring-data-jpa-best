package es.thefactory.hellospringdatajpa.biz.service;

import es.thefactory.hellospringdatajpa.biz.domain.Player;
import es.thefactory.hellospringdatajpa.biz.exception.PlayerNotFoundException;
import es.thefactory.hellospringdatajpa.biz.exception.TeamNotFoundException;
import es.thefactory.hellospringdatajpa.biz.mapper.PlayerMapper;
import es.thefactory.hellospringdatajpa.dal.entity.PlayerEntity;
import es.thefactory.hellospringdatajpa.dal.repo.PlayerRepository;
import es.thefactory.hellospringdatajpa.dal.repo.TeamRepository;
import es.thefactory.hellospringdatajpa.dal.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Pablo Lorenzo Manzano.
 */
@Service
@RequiredArgsConstructor
public class PlayerService {

    /**
     *
     */
    private final EntityValidator entityValidator;

    /**
     *
     */
    private final PlayerMapper playerMapper;

    /**
     *
     */
    private final PlayerRepository playerRepository;

    /**
     *
     */
    private final TeamRepository teamRepository;

    /**
     *
     * @param player
     * @return Player
     */
    @Transactional
    public Player create(Player player) {
        UUID teamId = player.teamId();

        if ((teamId != null) && !(teamRepository.existsById(teamId))) {
            throw new TeamNotFoundException();
        }

        PlayerEntity playerEntity = playerMapper.toEntity(player);
        entityValidator.validate(playerEntity);
        playerEntity = playerRepository.save(playerEntity);

        return (playerMapper.toDomain(playerEntity));
    }

    /**
     *
     * @param playerId
     */
    @Transactional
    public void deleteById(UUID playerId) {
        if (playerRepository.deleteByPlayerId(playerId) == 0) {
            throw new PlayerNotFoundException();
        }
    }

    /**
     *
     * @return List<Player>
     */
    public List<Player> getAll() {
        List<PlayerEntity> playerEntityList = playerRepository.findAll();

        return (playerMapper.toDomain(playerEntityList));
    }

    /**
     *
     * @param playerId
     * @return Optional<Player>
     */
    public Optional<Player> getById(UUID playerId) {
        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findById(playerId);

        return (optionalPlayerEntity.map(playerMapper::toDomain));
    }

    /**
     *
     * @param player
     */
    @Transactional
    public void update(Player player) {
        UUID teamId = player.teamId();

        if ((teamId != null) && !(teamRepository.existsById(teamId))) {
            throw new TeamNotFoundException();
        }

        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findById(player.playerId());
        PlayerEntity playerEntity = optionalPlayerEntity.orElseThrow(PlayerNotFoundException::new);
        playerMapper.updateEntity(player, playerEntity);
        entityValidator.validate(playerEntity);
    }
}
