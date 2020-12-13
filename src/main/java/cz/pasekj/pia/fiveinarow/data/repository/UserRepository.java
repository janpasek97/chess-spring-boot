package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    List<UserEntity> findAll();
}
