package cz.pasekj.pia.fiveinarow.users.services;

import java.util.List;

public interface CurrentUserService {
    String getCurrentUserName();
    String getCurrentUserEmail();
    List<String> getCurrentUserRoles();
}
