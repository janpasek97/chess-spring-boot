package cz.pasekj.pia.fiveinarow.users;

import lombok.Getter;

/**
 * Friends message used for WebSockets - friend requests notifications
 */
@Getter
public class FriendsMessage {
    /** Sender of the message */
    private final String from;
    /** Target user of the message */
    private final String to;
    /** Action represented by the message */
    private final FriendsMessageAction action;

    /**
     * Constructor
     * @param from sender username
     * @param to target username
     * @param action message action
     */
    public FriendsMessage(String from, String to, FriendsMessageAction action) {
        this.from = from;
        this.to = to;
        this.action = action;
    }

    /**
     * Enumeration of message actions
     */
    public enum FriendsMessageAction {
        FRIENDS_ADD, FRIENDS_ACCEPT, FRIENDS_REFUSE, FRIENDS_REMOVE
    }
}
