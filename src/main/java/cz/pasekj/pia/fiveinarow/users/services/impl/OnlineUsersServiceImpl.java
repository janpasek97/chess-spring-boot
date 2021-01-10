package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.users.UserInfo;
import cz.pasekj.pia.fiveinarow.users.services.OnlineUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Online users service implemenetation
 */
@Service("activeUsersService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestScope
public class OnlineUsersServiceImpl implements OnlineUsersService {

    /** Session registry for getting online users */
    private final SessionRegistry sessionRegistry;

    /** Set of logged in users -> serves as a cache */
    private Set<String> onlineUsers;

    @Override
    public boolean isUserLoggedIn(String username) {
        if(onlineUsers == null) {
            List<UserInfo> usersInfo = getLoggedInUsers();
            onlineUsers = new HashSet<>();
            usersInfo.forEach(userInfo -> onlineUsers.add(userInfo.username));
        }
        return onlineUsers.contains(username);
    }

    @Override
    public List<UserInfo> getLoggedInUsers() {
        List<UserInfo> activeUsers = new ArrayList<>();

        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof User) {
                User user = (User)principal;
                if(sessionRegistry.getAllSessions(user, false).size() > 0) {
                    activeUsers.add(new UserInfo(user.getUsername(), true, false));
                }
            }
        }
        return activeUsers;
    }

    @Override
    public List<UserInfo> getLoggedInUsers(String except) {
        return getLoggedInUsers().stream().filter(s -> !s.username.equals(except)).collect(Collectors.toList());
    }

    @Override
    public Page<UserInfo> getLoggedInUsers(Pageable pageable, String except) {
        List<UserInfo> onlineUsers = getLoggedInUsers(except);
        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), onlineUsers.size()));
        return new PageImpl<>(onlineUsers.subList(start, end), pageable, onlineUsers.size());
    }
}
