package cz.pasekj.pia.fiveinarow.authorization.services;

import java.util.List;

public interface RolesService {
    boolean hasRole(String role, String username);
    List<String> getRoles();
}
