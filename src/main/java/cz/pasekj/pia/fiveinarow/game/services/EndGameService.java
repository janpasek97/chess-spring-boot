package cz.pasekj.pia.fiveinarow.game.services;

/**
 * Service providing functionality to finish a game
 */
public interface EndGameService {

    /**
     * Finish a game of given ID
     * - delete important records from DB
     * - keep result in database
     * @param gameId id of the game to finishes
     */
    void finishGame(String gameId);
}
