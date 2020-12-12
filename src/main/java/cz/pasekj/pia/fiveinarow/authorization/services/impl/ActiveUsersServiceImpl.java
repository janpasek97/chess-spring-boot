package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.authorization.services.ActiveUsersService;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("activeUsersService")
@RequiredArgsConstructor
public class ActiveUsersServiceImpl implements ActiveUsersService {

    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    @Override
    public List<String> getLoggedInUsers() {
        List<String> activeUsers = new ArrayList<>();

        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof User) {
                User user = (User)principal;
                if(sessionRegistry.getAllSessions(user, false).size() > 0) {
                    activeUsers.add(user.getUsername());
                }
            }
        }
        return activeUsers;
    }
}
