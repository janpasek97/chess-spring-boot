package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;

import java.util.List;

public interface UserInfoService {
    UserEntity getCurrentUser();
    String getCurrentUserName();
    String getCurrentUserEmail();
    List<String> getCurrentUserRoles();

    String getEmailOf(String username);
    List<String> getRolesOf(String username);
}
