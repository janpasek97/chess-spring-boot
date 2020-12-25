package cz.pasekj.pia.fiveinarow.data.repository;

import cz.pasekj.pia.fiveinarow.data.entity.GameResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultsRepository extends JpaRepository<GameResultEntity, Long> {
}
