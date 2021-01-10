package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.users.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for getting list of all users
 */
public interface AllUsersService {
    /**
     * Get all users
     * @return List of UserInfo
     */
    List<UserInfo> getAllUsers();

    /**
     * Get all users
     * @param except user who not to include in the results
     * @return List of UserInfo
     */
    List<UserInfo> getAllUsers(String except);

    /**
     * Get page of all users
     * @param except user who not to include in the results
     * @param pageable pagination setup
     * @return List of UserInfo
     */
    Page<UserInfo> getAllUsers(Pageable pageable, String except);
}
