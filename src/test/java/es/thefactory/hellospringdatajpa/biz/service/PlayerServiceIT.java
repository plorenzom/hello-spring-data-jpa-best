package es.thefactory.hellospringdatajpa.biz.service;

import es.thefactory.hellospringdatajpa.biz.domain.Player;
import es.thefactory.hellospringdatajpa.biz.domain.PlayerBuilder;
import es.thefactory.hellospringdatajpa.biz.exception.PlayerNotFoundException;
import es.thefactory.hellospringdatajpa.biz.exception.TeamNotFoundException;
import es.thefactory.hellospringdatajpa.config.AppConfig;
import es.thefactory.hellospringdatajpa.dal.entity.PlayerEntity;
import es.thefactory.hellospringdatajpa.dal.entity.PlayerEntityBuilder;
import es.thefactory.hellospringdatajpa.dal.entity.TeamEntityBuilder;
import es.thefactory.hellospringdatajpa.dal.repo.PlayerRepository;
import es.thefactory.hellospringdatajpa.dal.repo.TeamRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Pablo Lorenzo Manzano.
 */
@SpringJUnitConfig(classes = AppConfig.class)
class PlayerServiceIT {

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
     */
    private final PlayerService playerService;

    /**
     *
     * @param playerRepository
     * @param teamRepository
     * @param playerService
     */
    @Autowired
    PlayerServiceIT(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerService playerService) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerService = playerService;
    }

    /**
     *
     */
    @BeforeEach
    void resetTestData() {
        playerRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
    }

    /**
     *
     */
    @Test
    void create_withAllFields_savesPlayer() {
        UUID teamId = teamRepository.save(TeamEntityBuilder.aTeam().build()).getTeamId();

        Player inputPlayer = PlayerBuilder.aPlayer().withTeamId(teamId).build();

        assertPlayerCreation(inputPlayer);
    }

    /**
     *
     */
    @Test
    void create_withOnlyRequiredFields_savesPlayer() {
        Player inputPlayer =
            PlayerBuilder.aPlayer().withTeamId(null).withMaternalSurname(null).withNickname(null).build();

        assertPlayerCreation(inputPlayer);
    }

    /**
     *
     */
    @Test
    void create_whenTeamDoesNotExist_throwsTeamNotFoundException() {
        UUID teamId = UUID.randomUUID();

        Player inputPlayer = PlayerBuilder.aPlayer().withTeamId(teamId).build();

        assertThatThrownBy(() -> playerService.create(inputPlayer)).isInstanceOf(TeamNotFoundException.class);

        assertThat(playerRepository.count()).isZero();
    }

    /**
     *
     */
    @Test
    void create_withInvalidPlayer_throwsConstraintViolationException() {
        Player inputPlayer = PlayerBuilder.aPlayer().withName(null).build();

        assertThatThrownBy(() -> playerService.create(inputPlayer)).isInstanceOf(ConstraintViolationException.class);

        assertThat(playerRepository.count()).isZero();
    }

    /**
     *
     */
    @Test
    void deleteById_withExistingId_deletesPlayer() {
        UUID playerId = playerRepository.save(PlayerEntityBuilder.aPlayer().build()).getPlayerId();

        playerService.deleteById(playerId);

        assertThat(playerRepository.existsById(playerId)).isFalse();
    }

    /**
     *
     */
    @Test
    void deleteById_withNonExistingId_throwsPlayerNotFoundException() {
        UUID playerId = UUID.randomUUID();

        assertThatThrownBy(() -> playerService.deleteById(playerId)).isInstanceOf(PlayerNotFoundException.class);
    }

    /**
     *
     */
    @Test
    void getAll_whenPlayersExist_returnsAllPlayers() {
        List<PlayerEntity> actualPlayerEntityList = List.of(
            PlayerEntityBuilder.aPlayer().build(),
            PlayerEntityBuilder.aPlayer()
                .withName("Jugador 2")
                .withPaternalSurname("Apellido 2")
                .withMaternalSurname("Apellido 2")
                .withNickname("Alias 2")
                .build());

        actualPlayerEntityList = playerRepository.saveAll(actualPlayerEntityList);

        List<Player> actualPlayerList = playerService.getAll();

        assertThat(actualPlayerList).hasSize(actualPlayerEntityList.size());
        assertThat(actualPlayerList).extracting(Player::playerId).containsExactlyInAnyOrder(
            actualPlayerEntityList.getFirst().getPlayerId(),
            actualPlayerEntityList.get(1).getPlayerId());
    }

    /**
     *
     */
    @Test
    void getAll_whenNoPlayersExist_returnsEmpty() {
        List<Player> actualPlayerList = playerService.getAll();

        assertThat(actualPlayerList).isEmpty();
    }

    /**
     *
     */
    @Test
    void getById_withExistingId_returnsPlayer() {
        PlayerEntity actualPlayerEntity = playerRepository.save(PlayerEntityBuilder.aPlayer().build());

        UUID playerId = actualPlayerEntity.getPlayerId();

        Optional<Player> optionalPlayer = playerService.getById(playerId);

        assertThat(optionalPlayer).isPresent();
        Player actualPlayer = optionalPlayer.get();
        assertThat(actualPlayer).usingRecursiveComparison().isEqualTo(actualPlayerEntity);
    }

    /**
     *
     */
    @Test
    void getById_withNonExistingId_returnsEmpty() {
        UUID playerId = UUID.randomUUID();

        Optional<Player> optionalPlayer = playerService.getById(playerId);

        assertThat(optionalPlayer).isEmpty();
    }

    /**
     *
     */
    @Test
    void update_whenPlayerExists_updatesPlayer() {
        UUID teamId = teamRepository.save(TeamEntityBuilder.aTeam().build()).getTeamId();

        UUID playerId = playerRepository.save(PlayerEntityBuilder.aPlayer().withTeamId(teamId).build()).getPlayerId();

        Player inputPlayer = PlayerBuilder.aPlayer()
            .withPlayerId(playerId)
            .withTeamId(null)
            .withName("Jugador X")
            .withPaternalSurname("Apellido X")
            .withMaternalSurname(null)
            .withNickname(null)
            .withBirthDate(LocalDate.of(1990, 1, 1))
            .withSex("M")
            .build();

        playerService.update(inputPlayer);

        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findById(playerId);

        assertThat(optionalPlayerEntity).isPresent();
        PlayerEntity actualPlayerEntity = optionalPlayerEntity.get();
        assertThat(actualPlayerEntity).usingRecursiveComparison().isEqualTo(inputPlayer);
    }

    /**
     *
     */
    @Test
    void update_whenTeamDoesNotExist_throwsTeamNotFoundException() {
        PlayerEntity actualPlayerEntity = playerRepository.save(PlayerEntityBuilder.aPlayer().build());

        UUID playerId = actualPlayerEntity.getPlayerId();
        UUID teamId = UUID.randomUUID();

        Player inputPlayer = PlayerBuilder.aPlayer().withPlayerId(playerId).withTeamId(teamId).build();

        assertThatThrownBy(() -> playerService.update(inputPlayer)).isInstanceOf(TeamNotFoundException.class);

        assertThat(playerRepository.findById(playerId).orElseThrow()).usingRecursiveComparison()
            .isEqualTo(actualPlayerEntity);
    }

    /**
     *
     */
    @Test
    void update_whenPlayerDoesNotExist_throwsPlayerNotFoundException() {
        UUID playerId = UUID.randomUUID();

        Player inputPlayer = PlayerBuilder.aPlayer().withPlayerId(playerId).build();

        assertThatThrownBy(() -> playerService.update(inputPlayer)).isInstanceOf(PlayerNotFoundException.class);
    }

    /**
     *
     */
    @Test
    void update_withInvalidPlayer_throwsConstraintViolationException() {
        PlayerEntity actualPlayerEntity = playerRepository.save(PlayerEntityBuilder.aPlayer().build());

        UUID playerId = actualPlayerEntity.getPlayerId();

        Player inputPlayer = PlayerBuilder.aPlayer().withPlayerId(playerId).withName(null).build();

        assertThatThrownBy(() -> playerService.update(inputPlayer)).isInstanceOf(ConstraintViolationException.class);

        assertThat(playerRepository.findById(playerId).orElseThrow()).usingRecursiveComparison()
            .isEqualTo(actualPlayerEntity);
    }

    /**
     *
     * @param player
     */
    private void assertPlayerCreation(Player player) {
        Player actualPlayer = playerService.create(player);

        assertThat(actualPlayer).isNotNull();
        assertThat(actualPlayer.playerId()).isNotNull();
        assertThat(actualPlayer).usingRecursiveComparison().ignoringFields("playerId").isEqualTo(player);

        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findById(actualPlayer.playerId());

        assertThat(optionalPlayerEntity).isPresent();
        PlayerEntity actualPlayerEntity = optionalPlayerEntity.get();
        assertThat(actualPlayerEntity).usingRecursiveComparison().ignoringFields("playerId").isEqualTo(player);
    }
}
