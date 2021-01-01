package cz.pasekj.pia.fiveinarow.game.services;

import cz.pasekj.pia.fiveinarow.data.entity.GameEntity;
import cz.pasekj.pia.fiveinarow.game.PlayerColor;

public interface InGameHandlerService {
    String getInGame(String playerEmail);
    boolean isInGame(String username);
    String getCompetitor(String gameId, String playerEmail);
    boolean performMove(String gameId, String playerEmail, int x, int y);
    PlayerColor[][] getBoard(String gameId);
    PlayerColor getPlayerOnMove(String gameId);
    String getWhitePlayerEmail(String gameId);
    String getBlackPlayerEmail(String gameId);
    PlayerColor getWin(String gameId);
    void terminateGame(String gameId);
}
