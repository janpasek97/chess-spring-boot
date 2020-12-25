package cz.pasekj.pia.fiveinarow.game.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.GameEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserInGameEntity;
import cz.pasekj.pia.fiveinarow.data.repository.GameRepository;
import cz.pasekj.pia.fiveinarow.data.repository.GameResultsRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserInGameRepository;
import cz.pasekj.pia.fiveinarow.game.PlayerColor;
import cz.pasekj.pia.fiveinarow.game.services.InGameHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("inGameHandlerService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InGameHandlerServiceImpl implements InGameHandlerService {

    private final GameRepository gameRepository;
    private final UserInGameRepository userInGameRepository;
    private final GameResultsRepository gameResultsRepository;

    @Override
    public String getInGame(String playerEmail) {
        Optional<UserInGameEntity> userInGameEntityOptional = userInGameRepository.findById(playerEmail);
        if (userInGameEntityOptional.isEmpty()) return null;
        return userInGameEntityOptional.get().getGameId();
    }

    @Override
    public String getCompetitor(String gameId, String playerEmail) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        if(gameEntityOptional.isEmpty()) return null;
        GameEntity gameEntity = gameEntityOptional.get();
        return gameEntity.getWhitePlayer().equals(playerEmail) ? gameEntity.getBlackPlayer() : gameEntity.getWhitePlayer();
    }

    @Override
    public boolean performMove(String gameId, String playerEmail, int x, int y) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        if(gameEntityOptional.isEmpty()) return false;
        GameEntity gameEntity =  gameEntityOptional.get();
        boolean returnVal = gameEntity.performMove(playerEmail, x, y);
        gameRepository.save(gameEntity);
        return returnVal;
    }

    @Override
    public PlayerColor[][] getBoard(String gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        if(gameEntityOptional.isEmpty()) return null;
        GameEntity gameEntity =  gameEntityOptional.get();
        return gameEntity.getBoard();
    }

    @Override
    public PlayerColor getPlayerOnMove(String gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        if(gameEntityOptional.isEmpty()) return null;
        GameEntity gameEntity =  gameEntityOptional.get();
        return gameEntity.getOnMove();
    }

    @Override
    public String getWhitePlayerEmail(String gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        if(gameEntityOptional.isEmpty()) return null;
        GameEntity gameEntity =  gameEntityOptional.get();
        return gameEntity.getWhitePlayer();
    }

    @Override
    public String getBlackPlayerEmail(String gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        if(gameEntityOptional.isEmpty()) return null;
        GameEntity gameEntity =  gameEntityOptional.get();
        return gameEntity.getBlackPlayer();
    }

    @Override
    public boolean isWin(String gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        if(gameEntityOptional.isEmpty()) return false;
        GameEntity gameEntity =  gameEntityOptional.get();
        return gameEntity.isWin();
    }

    @Override
    public void terminateGame(String gameId) {

    }
}
