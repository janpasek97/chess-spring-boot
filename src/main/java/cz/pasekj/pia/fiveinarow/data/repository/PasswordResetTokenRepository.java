package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.PasswordResetTokenEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    PasswordResetTokenEntity findByUser(UserEntity userEntity);
    PasswordResetTokenEntity findByToken(String token);
}
