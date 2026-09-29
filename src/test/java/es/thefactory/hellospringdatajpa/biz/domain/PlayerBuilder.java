package es.thefactory.hellospringdatajpa.biz.domain;

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
public final class PlayerBuilder {

    /**
     *
     */
    private UUID playerId = null;

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
     * @return PlayerBuilder
     */
    public static PlayerBuilder aPlayer() {
        return (new PlayerBuilder());
    }

    /**
     *
     * @return Player
     */
    public Player build() {
        return (Player.builder()
            .playerId(playerId)
            .teamId(teamId)
            .name(name)
            .paternalSurname(paternalSurname)
            .maternalSurname(maternalSurname)
            .nickname(nickname)
            .birthDate(birthDate)
            .sex(sex)
            .build());
    }
}
