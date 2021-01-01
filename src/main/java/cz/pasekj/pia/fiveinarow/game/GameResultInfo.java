package cz.pasekj.pia.fiveinarow.game;

import lombok.Getter;

@Getter
public class GameResultInfo {

    private final Result result;
    private final String timestamp;
    private final String opponent;

    public GameResultInfo(Result result, String timestamp, String opponent) {
        this.result = result;
        this.timestamp = timestamp;
        this.opponent = opponent;
    }

    public enum Result {
        WIN, DEFEAT
    }
}
