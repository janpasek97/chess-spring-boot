package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.UserInfo;
import cz.pasekj.pia.fiveinarow.users.services.FriendsService;
import cz.pasekj.pia.fiveinarow.users.services.OnlineUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("friendsService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FriendsServiceImpl implements FriendsService {

    private final UserRepository userRepository;
    private final OnlineUsersService onlineUsersService;

    @Override
    public boolean areFriends(String user1, String user2) {
        UserEntity user1Entity = userRepository.findByUsername(user1);
        UserEntity user2Entity = userRepository.findByUsername(user2);

        return      user1Entity.getFriendFrom().contains(user2Entity)
                &&  user1Entity.getFriendTo().contains(user2Entity);
    }

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

    @Override
    @Transactional
    public void removeFriend(String usernameFrom, String usernameTo) {
        UserEntity userFrom = userRepository.findByUsername(usernameFrom);
        UserEntity userTo = userRepository.findByUsername(usernameTo);

        if(userFrom != null && userTo != null) {
            userFrom.removeFriendTo(userTo);
            userFrom.removeFriendFrom(userTo);
            userRepository.save(userFrom);

            userTo.removeFriendFrom(userFrom);
            userTo.removeFriendTo(userFrom);
            userRepository.save(userTo);

            userRepository.flush();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfo> getFriendsOf(String username) {
        List<UserInfo> friends = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUsername(username);
        List<UserEntity> friendsFrom = userEntity.getFriendFrom();
        List<UserEntity> friendsTo = userEntity.getFriendTo();

        Set<UserEntity> friendsIntersection = friendsFrom
                .stream()
                .distinct()
                .filter(friendsTo::contains)
                .collect(Collectors.toSet());

        for (UserEntity user : friendsIntersection) {
            UserInfo friend = new UserInfo(user.getUsername(), onlineUsersService.isUserLoggedIn(user.getUsername()), true);
            friends.add(friend);
        }
        return friends;
    }

    @Override
    public Page<UserInfo> getFriendsOf(String username, Pageable pageable) {
        List<UserInfo> friends = getFriendsOf(username);
        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), friends.size()));
        return new PageImpl<>(friends.subList(start, end), pageable, friends.size());
    }
}
