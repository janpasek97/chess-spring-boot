package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.users.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AllUsersService {
    List<UserInfo> getAllUsers();
    List<UserInfo> getAllUsers(String except);
    Page<UserInfo> getAllUsers(Pageable pageable, String except);
}
