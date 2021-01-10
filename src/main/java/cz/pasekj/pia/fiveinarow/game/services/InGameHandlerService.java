package cz.pasekj.pia.fiveinarow.game.services;

import cz.pasekj.pia.fiveinarow.game.PlayerColor;

/**
 * Service handling gameplay
 */
public interface InGameHandlerService {
    /**
     * Get ID of the game where player is engaged
     * @param playerEmail email of the player
     * @return ID of the corresponding player / null if player is not in game
     */
    String getInGame(String playerEmail);

    /**
     * Check if player is in game
     * @param username username of the player
     * @return bool flag indicating whether a player is in a game
     */
    boolean isInGame(String username);

    /**
     * Get competitor of a given player in a given game
     * @param gameId ID of the game
     * @param playerEmail Email of the player
     * @return Email of the opponent
     */
    String getCompetitor(String gameId, String playerEmail);

    /**
     * Perform move
     * @param gameId ID of the game where to perform move
     * @param playerEmail email of the player who made the move
     * @param x x coordinate of the move
     * @param y y coordinate of the move
     * @return bool flag indicating success of the move
     */
    boolean performMove(String gameId, String playerEmail, int x, int y);

    /**
     * Get board of the game
     * @param gameId ID of the game
     * @return game board
     */
    PlayerColor[][] getBoard(String gameId);

    /**
     * Get color of a player who is on move
     * @param gameId ID of the game
     * @return color of a player who is on move
     */
    PlayerColor getPlayerOnMove(String gameId);

    /**
     * Get email of a white player
     * @param gameId ID of the game
     * @return email of the white player
     */
    String getWhitePlayerEmail(String gameId);

    /**
     * Get email of a black player
     * @param gameId ID of the game
     * @return email of the black player
     */
    String getBlackPlayerEmail(String gameId);

    /**
     * Get color of a player who won the game
     * @param gameId ID of the game
     * @return color of the  winner / null if there's no winner yet
     */
    PlayerColor getWin(String gameId);

    /**
     * Handler a situation when player surrendered the game
     * @param gameId ID of the game
     * @param playerEmail email of the player who surrendered
     */
    void surrender(String gameId, String playerEmail);
}
