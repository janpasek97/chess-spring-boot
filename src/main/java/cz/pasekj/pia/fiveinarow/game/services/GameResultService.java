package cz.pasekj.pia.fiveinarow.game.services;

import cz.pasekj.pia.fiveinarow.game.GameResultInfo;

import java.util.List;

/**
 * Game result service providing access to game results
 */
public interface GameResultService {
    /**
     * Get game results of the currently logged in user
     * @return List of GameResultInfo
     */
    List<GameResultInfo> currentUserResults();
}
