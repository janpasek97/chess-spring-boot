package cz.pasekj.pia.fiveinarow.users;

import lombok.Getter;

@Getter
public class FriendsMessage {
    private final String from;
    private final String to;
    private final FriendsMessageAction action;

    public FriendsMessage(String from, String to, FriendsMessageAction action) {
        this.from = from;
        this.to = to;
        this.action = action;
    }

    public enum FriendsMessageAction {
        FRIENDS_ADD, FRIENDS_ACCEPT, FRIENDS_REFUSE, FRIENDS_REMOVE
    }
}
