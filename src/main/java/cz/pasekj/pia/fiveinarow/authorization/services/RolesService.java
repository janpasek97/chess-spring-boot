package cz.pasekj.pia.fiveinarow.authorization.services;

import java.util.List;

/**
 * Service providing information about all roles and role validation functionality
 */
public interface RolesService {

    /**
     * Check whether the user has the role given
     * @param role role name to be checked
     * @param username username of the user to be checked
     * @return bool flag indicating whether the user has the role or not
     */
    boolean hasRole(String role, String username);

    /**
     * Get list of all possible roles
     * @return List of all possible roles
     */
    List<String> getRoles();
}
