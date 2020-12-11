package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.FriendsListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<FriendsListEntity, Long> {
}
