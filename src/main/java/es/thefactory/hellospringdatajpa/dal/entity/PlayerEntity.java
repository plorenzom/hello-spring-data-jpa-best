package es.thefactory.hellospringdatajpa.dal.entity;

import es.thefactory.hellospringdatajpa.dal.spi.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Pablo Lorenzo Manzano.
 */
@Entity
@Getter
@Setter
@Table(name = "player")
public class PlayerEntity implements Identifiable {

    /**
     *
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "player_id")
    private UUID playerId;

    /**
     *
     */
    @Column(name = "team_id")
    private UUID teamId;

    /**
     *
     */
    @NotBlank
    @Size(max = 25)
    @Column(name = "name", length = 25, nullable = false)
    private String name;

    /**
     *
     */
    @NotBlank
    @Size(max = 25)
    @Column(name = "paternal_surname", length = 25, nullable = false)
    private String paternalSurname;

    /**
     *
     */
    @Size(max = 25)
    @Column(name = "maternal_surname", length = 25)
    private String maternalSurname;

    /**
     *
     */
    @Size(max = 25)
    @Column(name = "nickname", length = 25)
    private String nickname;

    /**
     *
     */
    @NotNull
    @Past
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /**
     *
     */
    @NotBlank
    @Pattern(regexp = "^[HM]$")
    @Column(name = "sex", length = 1, nullable = false)
    private String sex;
}
