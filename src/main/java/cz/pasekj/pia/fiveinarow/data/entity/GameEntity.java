package cz.pasekj.pia.fiveinarow.data.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.pasekj.pia.fiveinarow.game.PlayerColor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Game")
public class GameEntity implements Serializable {
    private String id;
    private String whitePlayer;
    private String blackPlayer;
    private boolean isWin;
    private PlayerColor onMove;
    private String board;

    public GameEntity() {}

    public GameEntity(String id, String whitePlayer, String blackPlayer, String board) {
        this.id = id;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.onMove = PlayerColor.WHITE;
        this.isWin = false;
        this.board = board;
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

    public PlayerColor getWin() {
        if(!isWin) return null;
        return onMove;
    }

    public PlayerColor[][] getBoard() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PlayerColor[][] boardTmp = objectMapper.readValue(board, PlayerColor[][].class);
            return boardTmp;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void setBoard(PlayerColor[][] gameBoard) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.board = objectMapper.writeValueAsString(gameBoard);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public boolean performMove(String player, int x, int y) {
        if (isWin) return false;
        PlayerColor[][] gameBoard = getBoard();
        if(gameBoard == null) return false;
        int width = gameBoard.length;
        int height = gameBoard[0].length;

        PlayerColor playerColor;
        if(player.equals(whitePlayer)) playerColor = PlayerColor.WHITE;
        else playerColor = PlayerColor.BLACK;

        if (playerColor != onMove) return false;
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        if(gameBoard[x][y] != PlayerColor.EMPTY) return false;

        gameBoard[x][y] = onMove;
        isWin = checkWin(x, y, playerColor, gameBoard);

        if(!isWin) {
            onMove = onMove == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        }
        setBoard(gameBoard);
        return true;
    }

    private boolean checkWin(int x, int y, PlayerColor playerColor, PlayerColor[][] gameBoard) {
        return checkLines(x, y, playerColor, gameBoard) || checkDiagonals(x, y, playerColor, gameBoard);
    }

    private boolean checkLines(int x, int y, PlayerColor playerColor, PlayerColor[][] gameBoard) {
        int width = gameBoard.length;
        int height = gameBoard[0].length;
        int cntr = 0;
        // Check horizontal
        for (int ix = 0; ix < width; ix++) {
            if (gameBoard[ix][y] == playerColor) {
                cntr++;
                if(cntr >= 5) return true;
            }
            else cntr = 0;
        }

        // Check vertical
        cntr = 0;
        for (int iy = 0; iy < height; iy++) {
            if(gameBoard[x][iy] == playerColor) {
                cntr++;
                if(cntr >= 5) return true;
            }
            else cntr = 0;
        }

        return false;
    }

    private boolean checkDiagonals(int x, int y, PlayerColor playerColor, PlayerColor[][] gameBoard) {
        int width = gameBoard.length;
        int height = gameBoard[0].length;
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
            if(gameBoard[dx][dy] == playerColor) {
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
            if(gameBoard[ix][iy] == playerColor) {
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
