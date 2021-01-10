package cz.pasekj.pia.fiveinarow.game.services;

/**
 * Service for creating a new game
 */
public interface NewGameService {

    /**
     * Create a new game
     * - store information that the players are in game
     * - store game information
     * @param username1 username of the first user
     * @param username2 username of the second user
     * @param width width of the game board
     * @param height height of the game board
     */
    void createGame(String username1, String username2, int width, int height);

}
