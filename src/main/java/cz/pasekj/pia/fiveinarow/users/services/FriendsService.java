package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.users.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Friends service
 * Serves for creating/removing friendships
 */
public interface FriendsService {
    /**
     * Get information if two users are friends or no
     * @param user1 first user
     * @param user2 second user
     * @return bool indicating friendship status
     */
    boolean areFriends(String user1, String user2);

    /**
     * Request friendship from one user to another
     * @param usernameFrom requester user
     * @param usernameTo target user
     */
    void requestFriend(String usernameFrom, String usernameTo);

    /**
     * Confirm friendship from one user to another
     * @param usernameFrom requester user
     * @param usernameTo target user
     */
    void confirmFriend(String usernameFrom, String usernameTo);

    /**
     * Refuse friendship from one user to another
     * @param usernameFrom requester user
     * @param usernameTo target user
     */
    void refuseFriend(String usernameFrom, String usernameTo);

    /**
     * Remove friendship from one user to another
     * @param usernameFrom requester user
     * @param usernameTo target user
     */
    void removeFriend(String usernameFrom, String usernameTo);

    /**
     * Get all friends of given user
     * @param username user to be examined
     * @return List of UserInfo
     */
    List<UserInfo> getFriendsOf(String username);

    /**
     * Get page of friends of given user
     * @param username user to be examined
     * @param pageable pagination setup
     * @return List of UserEntity
     */
    Page<UserInfo> getFriendsOf(String username, Pageable pageable);

    /**
     * Get friends requests of given user
     * @param username user to be examined
     * @return list of UserInfo
     */
    List<UserInfo> getFriendsRequestsOf(String username);
}
