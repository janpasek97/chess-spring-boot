package cz.pasekj.pia.fiveinarow.game;

import lombok.Getter;

/**
 * Helper object for transferring the game result
 */
@Getter
public class GameResultInfo {

    /** Result of the game */
    private final Result result;
    /** Game result timestamp */
    private final String timestamp;
    /** Opponent username */
    private final String opponent;

    /**
     * Constructor
     * @param result result of the game
     * @param timestamp timestamp
     * @param opponent opponent username
     */
    public GameResultInfo(Result result, String timestamp, String opponent) {
        this.result = result;
        this.timestamp = timestamp;
        this.opponent = opponent;
    }

    /**
     * Enumeration representing result - win or lose
     */
    public enum Result {
        WIN, DEFEAT
    }
}
