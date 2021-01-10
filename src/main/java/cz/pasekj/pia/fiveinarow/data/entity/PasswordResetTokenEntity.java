package cz.pasekj.pia.fiveinarow.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Entity representing password reset token
 * Stored in Postgres DB
 */
@Entity
@Table(name = "password_reset_token")
public class PasswordResetTokenEntity {

    /** Expiration of the toke in minutes */
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Password reset token */
    private String token;

    /** User associated with the reset token */
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    /** Password reset token expiration date and time */
    private Date expiryDate;

    /**
     * Constructor - used by the framework
     */
    public PasswordResetTokenEntity() {
    }

    /**
     * Constructor
     * @param token auto generated password reset token (eg. UUID)
     * @param user UserEntity associated with the reset token
     */
    public PasswordResetTokenEntity(String token, UserEntity user) {
        this.token = token;
        this.user = user;
        this.expiryDate = new Date(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)*1000 + EXPIRATION*60*1000);
    }

    /**
     * Get reset token
     * @return password reset token
     */
    public String getToken() {
        return token;
    }

    /**
     * Get user associated with the reset token
     * @return  UserEntity associated with the reset token
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Get expiration date of the reset token
     * @return date of the token expiration
     */
    public Date getExpiryDate() {
        return expiryDate;
    }
}
