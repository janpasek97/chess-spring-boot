package cz.pasekj.pia.fiveinarow.data.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class GameResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "timestamp")
    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name="winner_id", nullable = false)
    private UserEntity winner;

    @ManyToOne
    @JoinColumn(name = "loser_id", nullable = false)
    private UserEntity loser;

    public GameResultEntity(){};

    public GameResultEntity(UserEntity winner, UserEntity loser) {
        this.winner = winner;
        this.loser = loser;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public UserEntity getWinner() {
        return winner;
    }

    public UserEntity getLoser() {
        return loser;
    }
}
