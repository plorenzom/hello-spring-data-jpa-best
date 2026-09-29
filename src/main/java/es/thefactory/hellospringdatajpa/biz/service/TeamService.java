package es.thefactory.hellospringdatajpa.biz.service;

import es.thefactory.hellospringdatajpa.biz.domain.Team;
import es.thefactory.hellospringdatajpa.biz.exception.TeamHasPlayersException;
import es.thefactory.hellospringdatajpa.biz.exception.TeamNotFoundException;
import es.thefactory.hellospringdatajpa.biz.mapper.TeamMapper;
import es.thefactory.hellospringdatajpa.dal.entity.TeamEntity;
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
public class TeamService {

    /**
     *
     */
    private final EntityValidator entityValidator;

    /**
     *
     */
    private final TeamMapper teamMapper;

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
     * @param team
     * @return Team
     */
    public Team create(Team team) {
        TeamEntity teamEntity = teamMapper.toEntity(team);
        entityValidator.validate(teamEntity);
        teamEntity = teamRepository.save(teamEntity);

        return (teamMapper.toDomain(teamEntity));
    }

    /**
     *
     * @param teamId
     */
    @Transactional
    public void deleteById(UUID teamId) {
        if (playerRepository.existsByTeamId(teamId)) {
            throw new TeamHasPlayersException();
        }

        if (teamRepository.deleteByTeamId(teamId) == 0) {
            throw new TeamNotFoundException();
        }
    }

    /**
     *
     * @return List<Team>
     */
    public List<Team> getAll() {
        List<TeamEntity> teamEntityList = teamRepository.findAll();

        return (teamMapper.toDomain(teamEntityList));
    }

    /**
     *
     * @param teamId
     * @return Optional<Team>
     */
    public Optional<Team> getById(UUID teamId) {
        Optional<TeamEntity> optionalTeamEntity = teamRepository.findById(teamId);

        return (optionalTeamEntity.map(teamMapper::toDomain));
    }

    /**
     *
     * @param team
     */
    public void update(Team team) {
        Optional<TeamEntity> optionalTeamEntity = teamRepository.findById(team.teamId());
        TeamEntity teamEntity = optionalTeamEntity.orElseThrow(TeamNotFoundException::new);
        teamMapper.updateEntity(team, teamEntity);
        entityValidator.validate(teamEntity);
        teamRepository.save(teamEntity);
    }
}
