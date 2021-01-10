package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.users.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for checking if user is online and getting lists of online users
 */
public interface OnlineUsersService {
    /**
     * Check if the given user is logged in
     * @param username user to be examined
     * @return bool indicating online status
     */
    boolean isUserLoggedIn(String username);

    /**
     * Get all logged in user
     * @return List of UserInfo
     */
    List<UserInfo> getLoggedInUsers();

    /**
     * Get all logged in users except the given one
     * @param except user who not to include in the results
     * @returnList of UserInfo
     */
    List<UserInfo> getLoggedInUsers(String except);

    /**
     * Get page of logged in users except the given one
     * @param except user who not to include in the results
     * @param pageable pagination setup
     * @returnList of UserInfo
     */
    Page<UserInfo> getLoggedInUsers(Pageable pageable, String except);
}
