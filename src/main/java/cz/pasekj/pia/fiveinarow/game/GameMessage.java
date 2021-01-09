package cz.pasekj.pia.fiveinarow.game;

import lombok.Getter;

@Getter
public class GameMessage {
    private GameMessageAction action = null;
    private String opponent = "";
    private PlayerColor playerColor = null;
    private PlayerColor playerOnMove = null;
    private int x = 0;
    private int y = 0;
    private PlayerColor[][] board = null;
    private String message = "";

    public GameMessage() {}

    public GameMessage(GameMessageAction action, PlayerColor playerColor, int x, int y) {
        this.action = action;
        this.playerColor = playerColor;
        this.x = x;
        this.y = y;
    }

    public GameMessage(GameMessageAction action, String opponent, PlayerColor playerColor, PlayerColor playerOnMove, PlayerColor[][] board) {
        this.action = action;
        this.opponent = opponent;
        this.playerOnMove = playerOnMove;
        this.playerColor = playerColor;
        this.board = board;
    }

    public GameMessage(GameMessageAction action, String opponent, int x, int y) {
        this.action = action;
        this.opponent = opponent;
        this.x = x;
        this.y = y;
    }

    public GameMessage(GameMessageAction action) {
        this.action = action;
    }

    public enum GameMessageAction {
        MOVE, COUNTER_MOVE, CONNECT, CONNECT_DATA, START, ACCEPT, WIN, LOSE, MESSAGE
    }
}
