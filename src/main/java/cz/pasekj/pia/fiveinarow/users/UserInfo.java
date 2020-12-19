package cz.pasekj.pia.fiveinarow.users;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserInfo implements Serializable {

    public String username;
    public boolean online = false;
    public boolean friend = false;

    public UserInfo() { }

    public UserInfo(String username, boolean online, boolean friend) {
        this.username = username;
        this.online = online;
        this.friend = friend;
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
