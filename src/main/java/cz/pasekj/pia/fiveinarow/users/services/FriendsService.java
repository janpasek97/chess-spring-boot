package cz.pasekj.pia.fiveinarow.users.services;

import cz.pasekj.pia.fiveinarow.users.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendsService {
    boolean areFriends(String user1, String user2);
    void requestFriend(String usernameFrom, String usernameTo);
    void confirmFriend(String usernameFrom, String usernameTo);
    void refuseFriend(String usernameFrom, String usernameTo);
    void removeFriend(String usernameFrom, String usernameTo);
    List<UserInfo> getFriendsOf(String username);
    Page<UserInfo> getFriendsOf(String username, Pageable pageable);
    List<UserInfo> getFriendsRequestsOf(String username);
}
