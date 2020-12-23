package cz.pasekj.pia.fiveinarow.data.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("UserInGame")
public class UserInGameEntity implements Serializable {
    private final String id;
    private final String gameId;

    public UserInGameEntity(String id, String gameId) {
        this.id = id;
        this.gameId = gameId;
    }

    public String getId() {
        return id;
    }

    public String getGameId() {
        return gameId;
    }
}
