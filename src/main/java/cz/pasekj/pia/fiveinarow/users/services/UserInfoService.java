package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;

import java.util.List;

/**
 * Service for getting information about the current user
 */
public interface UserInfoService {

    /**
     * Get current user
     * @return UserEntity of the current user
     */
    UserEntity getCurrentUser();

    /**
     * Get username of the current user
     * @return username
     */
    String getCurrentUserName();

    /**
     * Get email of the current user
     * @return email
     */
    String getCurrentUserEmail();

    /**
     * Get roles of the current user
     * @return roles of the current user as string
     */
    List<String> getCurrentUserRoles();

    /**
     * Get email of a user given by a username
     * @param username username of the user
     * @return email of the user / null if the user does not exists
     */
    String getEmailOf(String username);

    /**
     * Get roles of the user given by the username
     * @param username username of the user
     * @return list of roles of the given user as string / null if the user does not exists
     */
    List<String> getRolesOf(String username);
}
