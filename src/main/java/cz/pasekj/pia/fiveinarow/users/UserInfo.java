package cz.pasekj.pia.fiveinarow.users;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class UserInfo implements Serializable {

    public String username;
    public String email;
    public String[] roles;
    public boolean inGame = false;
    public boolean online = false;
    public boolean friend = false;

    public UserInfo() { }

    public UserInfo(String username, boolean online, boolean friend) {
        this.username = username;
        this.online = online;
        this.friend = friend;
    }

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
