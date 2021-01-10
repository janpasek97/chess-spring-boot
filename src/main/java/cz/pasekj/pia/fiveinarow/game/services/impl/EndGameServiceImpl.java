package cz.pasekj.pia.fiveinarow.game.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.GameEntity;
import cz.pasekj.pia.fiveinarow.data.entity.GameResultEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserInGameEntity;
import cz.pasekj.pia.fiveinarow.data.repository.GameRepository;
import cz.pasekj.pia.fiveinarow.data.repository.GameResultsRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserInGameRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.game.PlayerColor;
import cz.pasekj.pia.fiveinarow.game.services.EndGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * EndGame service implementation
 */
@Service("endGameService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class EndGameServiceImpl implements EndGameService {

    /** UserInGameEntity DAO */
    private final UserInGameRepository userInGameRepository;
    /** GameEntity DAO */
    private final GameRepository gameRepository;
    /** UserEntity DAO */
    private final UserRepository userRepository;
    /** GameResultEntity DAO */
    private final GameResultsRepository gameResultsRepository;

    @Override
    @Transactional
    public void finishGame(String gameId) {
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if(gameOptional.isEmpty()) return;

        GameEntity game = gameOptional.get();
        PlayerColor winner = game.getWin();
        if(winner == null) return;

        String whitePlayer = game.getWhitePlayer();
        String blackPlayer = game.getBlackPlayer();

        Optional<UserInGameEntity> whiteUserInGame = userInGameRepository.findById(whitePlayer);
        whiteUserInGame.ifPresent(userInGameRepository::delete);

        Optional<UserInGameEntity> blackUserInGame = userInGameRepository.findById(blackPlayer);
        blackUserInGame.ifPresent(userInGameRepository::delete);

        gameRepository.delete(game);

        UserEntity winnerUser;
        UserEntity loserUser;
        if(winner == PlayerColor.WHITE) {
            winnerUser = userRepository.findByEmail(whitePlayer);
            loserUser = userRepository.findByEmail(blackPlayer);
        } else {
            winnerUser = userRepository.findByEmail(blackPlayer);
            loserUser = userRepository.findByEmail(whitePlayer);
        }

        GameResultEntity gameResultEntity = new GameResultEntity(winnerUser, loserUser);
        gameResultsRepository.saveAndFlush(gameResultEntity);
    }
}
