package cz.pasekj.pia.fiveinarow.data.entity;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Entity representing UserInGame information
 * Stored in redis
 */
@RedisHash("UserInGame")
public class UserInGameEntity implements Serializable {

    /** ID of the record = User's email */
    private final String id;
    /** ID of the game where the corresponding user is engaged in */
    private final String gameId;

    /**
     * Constructor
     * @param id user's email
     * @param gameId game ID (auto generated UUID referencing GameEntity)
     */
    public UserInGameEntity(String id, String gameId) {
        this.id = id;
        this.gameId = gameId;
    }

    /**
     * Get ID - user's email
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Get game ID referencing GameEntity
     * @return game ID
     */
    public String getGameId() {
        return gameId;
    }
}
