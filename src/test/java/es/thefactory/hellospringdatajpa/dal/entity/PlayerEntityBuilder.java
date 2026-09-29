package es.thefactory.hellospringdatajpa.dal.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Pablo Lorenzo Manzano.
 */
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerEntityBuilder {

    /**
     *
     */
    private UUID teamId = null;

    /**
     *
     */
    private String name = "Jugador 1";

    /**
     *
     */
    private String paternalSurname = "Apellido 1";

    /**
     *
     */
    private String maternalSurname = "Apellido 1";

    /**
     *
     */
    private String nickname = "Alias 1";

    /**
     *
     */
    private LocalDate birthDate = LocalDate.of(2000, 1, 1);

    /**
     *
     */
    private String sex = "H";

    /**
     *
     * @return PlayerEntityBuilder
     */
    public static PlayerEntityBuilder aPlayer() {
        return (new PlayerEntityBuilder());
    }

    /**
     *
     * @return PlayerEntity
     */
    public PlayerEntity build() {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setTeamId(teamId);
        playerEntity.setName(name);
        playerEntity.setPaternalSurname(paternalSurname);
        playerEntity.setMaternalSurname(maternalSurname);
        playerEntity.setNickname(nickname);
        playerEntity.setBirthDate(birthDate);
        playerEntity.setSex(sex);

        return playerEntity;
    }
}
