package cz.pasekj.pia.fiveinarow.game;

import lombok.Getter;

@Getter
public class GameMessage {
    private GameMessageAction action = null;
    private PlayerColor playerColor = null;
    private PlayerColor playerOnMove = null;
    private int x = 0;
    private int y = 0;
    private PlayerColor[][] board = null;

    public GameMessage() {}

    public GameMessage(GameMessageAction action, PlayerColor playerColor, int x, int y) {
        this.action = action;
        this.playerColor = playerColor;
        this.x = x;
        this.y = y;
    }

    public GameMessage(GameMessageAction action, PlayerColor playerColor, PlayerColor playerOnMove, PlayerColor[][] board) {
        this.action = action;
        this.playerOnMove = playerOnMove;
        this.playerColor = playerColor;
        this.board = board;
    }

    public enum GameMessageAction {
        MOVE, COUNTER_MOVE, CONNECT, CONNECT_DATA
    }
}
