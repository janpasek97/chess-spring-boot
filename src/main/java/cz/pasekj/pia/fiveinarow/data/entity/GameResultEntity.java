package cz.pasekj.pia.fiveinarow.data.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a GameResult
 * Stored in Postgres DB
 */
@Entity
@Table(name = "results")
public class GameResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Automatic timestamp of creating the record */
    @Column(name = "timestamp")
    @CreationTimestamp
    private LocalDateTime timestamp;

    /** Player who is the winner */
    @ManyToOne
    @JoinColumn(name="winner_id", nullable = false)
    private UserEntity winner;

    /** Player who is the loser */
    @ManyToOne
    @JoinColumn(name = "loser_id", nullable = false)
    private UserEntity loser;

    /**
     * Constructor - used by the framework
     */
    public GameResultEntity(){};

    /**
     * Constructor
     * @param winner player who won
     * @param loser player who lost
     */
    public GameResultEntity(UserEntity winner, UserEntity loser) {
        this.winner = winner;
        this.loser = loser;
    }

    /**
     * Get timestamp of the result
     * @return creation timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Get winner
     * @return UserEntity who won
     */
    public UserEntity getWinner() {
        return winner;
    }

    /**
     * Get loser
     * @return UserEntity who lose
     */
    public UserEntity getLoser() {
        return loser;
    }
}
