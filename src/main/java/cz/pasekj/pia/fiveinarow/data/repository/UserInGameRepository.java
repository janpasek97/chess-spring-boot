package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.UserInGameEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserInGameRepository extends CrudRepository<UserInGameEntity, String> {
}
