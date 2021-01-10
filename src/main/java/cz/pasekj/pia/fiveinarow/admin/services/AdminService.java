package cz.pasekj.pia.fiveinarow.admin.services;

import cz.pasekj.pia.fiveinarow.admin.AdminServiceResult;

import java.util.List;

/**
 * AdminService definition
 * Serves for changing user details by admin users
 */
public interface AdminService {
    /**
     * Change password of a user
     * @param email email of a user to be changed
     * @param password new user's password
     * @return instance of AdminServiceResult notifying about the change result
     */
    AdminServiceResult changePassword(String email, String password);

    /**
     * Change username of a user
     * @param email email of a user to be changed
     * @param username bew user's username
     * @return instance of AdminServiceResult notifying about the change result
     */
    AdminServiceResult changeUsername(String email, String username);

    /**
     * Change user's roles
     * @param email email of a user to be changed
     * @param roles a complete list of roles of the user to set
     * @return instance of AdminServiceResult notifying about the change result
     */
    AdminServiceResult changeRoles(String email, List<String> roles);
}
