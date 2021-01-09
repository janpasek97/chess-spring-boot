package cz.pasekj.pia.fiveinarow.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetTokenEntity {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    private Date expiryDate;

    public PasswordResetTokenEntity() {
    }

    public PasswordResetTokenEntity(String token, UserEntity user) {
        this.token = token;
        this.user = user;
        this.expiryDate = new Date(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)*1000 + EXPIRATION*60*1000);
    }

    public String getToken() {
        return token;
    }

    public UserEntity getUser() {
        return user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
}
