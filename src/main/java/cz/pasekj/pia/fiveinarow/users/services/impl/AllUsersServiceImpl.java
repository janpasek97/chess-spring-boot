package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.UserInfo;
import cz.pasekj.pia.fiveinarow.users.services.AllUsersService;
import cz.pasekj.pia.fiveinarow.users.services.OnlineUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("allUsersService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AllUsersServiceImpl implements AllUsersService {

    private final UserRepository userRepository;
    private final OnlineUsersService onlineUsersService;


    @Override
    public List<UserInfo> getAllUsers() {
        List<UserInfo> allUser = new ArrayList<>();
        List<UserEntity> allUsers = userRepository.findAll();
        allUsers.forEach(userEntity -> {
            UserInfo userInfo = new UserInfo();
            userInfo.username = userEntity.getUsername();
            userInfo.online = onlineUsersService.isUserLoggedIn(userInfo.username);
            allUser.add(userInfo);
        });
        return allUser;
    }

    @Override
    public List<UserInfo> getAllUsers(String except) {
        return getAllUsers().stream().filter(s -> !s.username.equals(except)).collect(Collectors.toList());
    }

    @Override
    public Page<UserInfo> getAllUsers(Pageable pageable, String except) {
        List<UserInfo> allUsers = getAllUsers(except);
        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), allUsers.size()));
        return new PageImpl<>(allUsers.subList(start, end), pageable, allUsers.size());
    }
}
