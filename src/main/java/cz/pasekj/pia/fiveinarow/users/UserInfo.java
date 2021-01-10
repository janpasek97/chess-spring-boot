package cz.pasekj.pia.fiveinarow.users;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Support data class for passing UserInformation to UI
 */
@Getter
public class UserInfo implements Serializable {

    /** User name */
    public String username;
    /** User email */
    public String email;
    /** List of user roles as string */
    public String[] roles;
    /** Indicate whether user is in game or not */
    public boolean inGame = false;
    /** Indicate whether user is online or not */
    public boolean online = false;
    /** Indicate whether user is a fried to current user or no */
    public boolean friend = false;

    /**
     * Constructor - used by framework
     */
    public UserInfo() { }

    /**
     * Constructor
     * @param username user name
     * @param online is user online
     * @param friend is user friend to current user
     */
    public UserInfo(String username, boolean online, boolean friend) {
        this.username = username;
        this.online = online;
        this.friend = friend;
    }

    /**
     * Set user roles
     * @param roles list of roles as string representation
     */
    public void setRoles(List<String> roles) {
        this.roles = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            this.roles[i] = roles.get(i);
        }
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserInfo) {
            return username.equals(((UserInfo) obj).username);
        } else {
            return username.equals(obj);
        }
    }
}
