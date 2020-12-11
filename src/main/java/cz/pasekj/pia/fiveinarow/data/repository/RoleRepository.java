package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
