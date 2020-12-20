package cz.pasekj.pia.fiveinarow.users.services;

import java.util.List;

public interface UserInfoService {
    String getCurrentUserName();
    String getCurrentUserEmail();
    List<String> getCurrentUserRoles();

    String getEmailOf(String username);
    List<String> getRolesOf(String username);
}
