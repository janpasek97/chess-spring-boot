package cz.pasekj.pia.fiveinarow.game;

import cz.pasekj.pia.fiveinarow.data.entity.GameEntity;
import lombok.Getter;

@Getter
public class GameMessage {
    GameMessageAction action;
    private final PlayerColor playerColor;
    private final int x;
    private final int y;

    public GameMessage(PlayerColor playerColor, int x, int y) {
        this.playerColor = playerColor;
        this.x = x;
        this.y = y;
    }

    public enum GameMessageAction {
        MOVE, COUNTER_MOVE
    }
}
