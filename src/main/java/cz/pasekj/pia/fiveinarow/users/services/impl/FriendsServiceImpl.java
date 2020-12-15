package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.services.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("friendsService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FriendsServiceImpl implements FriendsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void requestFriend(String usernameFrom, String usernameTo) {
        UserEntity userFrom = userRepository.findByUsername(usernameFrom);
        UserEntity userTo = userRepository.findByUsername(usernameTo);

        if(userFrom != null && userTo != null) {
            userFrom.addFriendTo(userTo);
            userRepository.save(userFrom);

            userTo.addFriendFrom(userFrom);
            userRepository.saveAndFlush(userTo);

            userRepository.flush();
        }
    }

    @Override
    @Transactional
    public void confirmFriend(String usernameFrom, String usernameTo) {
        UserEntity userFrom = userRepository.findByUsername(usernameFrom);
        UserEntity userTo = userRepository.findByUsername(usernameTo);

        if(userFrom != null && userTo != null) {
            userTo.addFriendTo(userFrom);
            userRepository.save(userTo);

            userFrom.addFriendFrom(userTo);
            userRepository.save(userFrom);

            userRepository.flush();
        }
    }

    @Override
    @Transactional
    public void refuseFriend(String usernameFrom, String usernameTo) {
        UserEntity userFrom = userRepository.findByUsername(usernameFrom);
        UserEntity userTo = userRepository.findByUsername(usernameTo);

        if(userFrom != null && userTo != null) {
            userFrom.removeFriendTo(userTo);
            userRepository.save(userFrom);

            userTo.removeFriendFrom(userFrom);
            userRepository.save(userTo);

            userRepository.flush();
        }
    }
}
