package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.users.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OnlineUsersService {
    boolean isUserLoggedIn(String username);
    List<UserInfo> getLoggedInUsers();
    List<UserInfo> getLoggedInUsers(String except);
    Page<UserInfo> getLoggedInUsers(Pageable pageable, String except);
}
