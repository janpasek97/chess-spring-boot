package cz.pasekj.pia.fiveinarow.users;

import java.io.Serializable;

public class UserInfo implements Serializable {

    public String username;
    public boolean online;

    public UserInfo() { }

    public UserInfo(String username, boolean online) {
        this.username = username;
        this.online = online;
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
