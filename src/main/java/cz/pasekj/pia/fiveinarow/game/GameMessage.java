package cz.pasekj.pia.fiveinarow.game;

import lombok.Getter;

/**
 * GameMessage implementation
 * Serves for data transfer between APP a JS
 */
@Getter
public class GameMessage {
    /** Action of the message */
    private GameMessageAction action = null;
    /** Information about the opponent */
    private String opponent = "";
    /** Color of the player who is the message sent to */
    private PlayerColor playerColor = null;
    /** Color of a player on move */
    private PlayerColor playerOnMove = null;
    /** X coordinate of the move or width */
    private int x = 0;
    /** Y coordinate of the move or height */
    private int y = 0;
    /** Board state */
    private PlayerColor[][] board = null;
    /** Message for in game chat */
    private String message = "";

    /**
     * Constructor - used by framework
     */
    public GameMessage() {}

    /**
     * Constructor
     * @param action message action
     * @param playerColor color of the player
     * @param x x coordinate of the move
     * @param y y coordinate of the move
     */
    public GameMessage(GameMessageAction action, PlayerColor playerColor, int x, int y) {
        this.action = action;
        this.playerColor = playerColor;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor
     * @param action message action
     * @param opponent opponents name
     * @param playerColor color of the player
     * @param playerOnMove color of a player on move
     * @param board board  state
     */
    public GameMessage(GameMessageAction action, String opponent, PlayerColor playerColor, PlayerColor playerOnMove, PlayerColor[][] board) {
        this.action = action;
        this.opponent = opponent;
        this.playerOnMove = playerOnMove;
        this.playerColor = playerColor;
        this.board = board;
    }

    /**
     * Constructor
     * @param action message action
     * @param opponent opponents name
     * @param x x coordinate of the move
     * @param y y coordinate of the move
     */
    public GameMessage(GameMessageAction action, String opponent, int x, int y) {
        this.action = action;
        this.opponent = opponent;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor
     * @param action message action
     */
    public GameMessage(GameMessageAction action) {
        this.action = action;
    }

    /**
     * Message actions enumeration
     */
    public enum GameMessageAction {
        MOVE, COUNTER_MOVE, CONNECT, CONNECT_DATA, START, ACCEPT, WIN, LOSE, MESSAGE, SURRENDER
    }
}
