package cz.pasekj.pia.fiveinarow.users.services;

public interface FriendsService {
    public void requestFriend(String usernameFrom, String usernameTo);
    public void confirmFriend(String usernameFrom, String usernameTo);
    public void refuseFriend(String usernameFrom, String usernameTo);
}
