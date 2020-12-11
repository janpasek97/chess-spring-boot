package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
