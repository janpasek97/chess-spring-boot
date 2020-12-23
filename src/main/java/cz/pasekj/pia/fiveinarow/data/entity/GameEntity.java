package cz.pasekj.pia.fiveinarow.data.entity;

import cz.pasekj.pia.fiveinarow.game.PlayerColor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Game")
public class GameEntity implements Serializable {
    private final String id;
    private final String whitePlayer;
    private final String blackPlayer;
    private final int width;
    private final int height;
    private boolean isWin;
    private PlayerColor onMove;
    private PlayerColor[][] board;

    public GameEntity(String id, String whitePlayer, String blackPlayer, int width, int height) {
        this.id = id;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.onMove = PlayerColor.WHITE;
        this.isWin = false;
        this.width = width;
        this.height = height;
        this.board = new PlayerColor[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = PlayerColor.EMPTY;
            }
        }

    }

    public String getId() {
        return id;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public PlayerColor getOnMove() {
        return onMove;
    }

    public boolean isWin() {
        return isWin;
    }

    public boolean performMove(String player, int x, int y) {
        if (isWin) return false;
        PlayerColor playerColor;
        if(player.equals(whitePlayer)) playerColor = PlayerColor.WHITE;
        else playerColor = PlayerColor.BLACK;

        if (playerColor != onMove) return false;
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        if(board[x][y] != PlayerColor.EMPTY) return false;

        board[x][y] = onMove;
        isWin = checkWin(x, y, playerColor);

        if(!isWin) {
            onMove = onMove == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        }

        return true;
    }

    private boolean checkWin(int x, int y, PlayerColor playerColor) {
        return checkLines(x, y, playerColor) || checkDiagonals(x, y, playerColor);
    }

    private boolean checkLines(int x, int y, PlayerColor playerColor) {
        int cntr = 0;
        // Check horizontal
        for (int ix = 0; ix < width; ix++) {
            if (board[ix][y] == playerColor) {
                cntr++;
                if(cntr >= 5) return true;
            }
            else cntr = 0;
        }

        // Check vertical
        cntr = 0;
        for (int iy = 0; iy < height; iy++) {
            if(board[x][iy] == playerColor) {
                cntr++;
                if(cntr >= 5) return true;
            }
            else cntr = 0;
        }

        return false;
    }

    private boolean checkDiagonals(int x, int y, PlayerColor playerColor) {
        // Check diagonal
        int dx;
        int dy;
        if(x >= y) {
            dx = x - y;
            dy = 0;
        } else {
            dx = 0;
            dy = y - x;
        }

        int cntr = 0;
        while (dx < width && dy < height) {
            if(board[dx][dy] == playerColor) {
                cntr++;
                if (cntr >= 5) return true;
            }
            else cntr = 0;
            dx++;
            dy++;
        }

        // Check inverse diagonal
        int ix;
        int iy;
        if(x + y < width) {
            ix = x + y;
            iy = 0;
        } else {
            ix = width - 1;
            iy = (x + y) - ix;
        }

        cntr = 0;
        while (ix >= 0 && iy < height) {
            if(board[ix][iy] == playerColor) {
                cntr++;
                if(cntr >= 5) return true;
            }
            else cntr = 0;

            ix--;
            iy++;
        }

        return false;
    }

}
