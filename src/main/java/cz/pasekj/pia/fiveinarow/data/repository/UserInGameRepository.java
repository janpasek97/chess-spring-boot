package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.UserInGame;
import org.springframework.data.repository.CrudRepository;

public interface UserInGameRepository extends CrudRepository<UserInGame, String> {
}
