package cz.pasekj.pia.fiveinarow.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "friends_list",
            joinColumns = @JoinColumn(name = "from_user_fk", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "to_user_fk", referencedColumnName = "id")
    )
    private List<UserEntity> friendsFrom;

    @ManyToMany(mappedBy = "friendsFrom")
    private List<UserEntity> friendsTo;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_has_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<RoleEntity> roles;

    public UserEntity() { }

    public UserEntity(String username, String email, String password, boolean enabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<UserEntity> getFriendTo() {
        return friendsTo;
    }

    public List<UserEntity> getFriendFrom() {
        return friendsFrom;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void clearRoles() {
        if(this.roles == null) this.roles = new ArrayList<>();
        this.roles.clear();
    }

    public void addRole(RoleEntity... roles) {
        if(this.roles == null) this.roles = new ArrayList<>();
        this.roles.addAll(Arrays.asList(roles));
    }

    public void addFriendTo(UserEntity to) {
        if(this.friendsTo == null) this.friendsTo = new ArrayList<>();
        this.friendsTo.add(to);
    }

    public void addFriendFrom(UserEntity from) {
        if(this.friendsFrom == null) this.friendsFrom = new ArrayList<>();
        this.friendsFrom.add(from);
    }

    public void removeFriendTo(UserEntity to) {
        if(this.friendsTo == null) this.friendsTo = new ArrayList<>();
        this.friendsTo.remove(to);
    }

    public void removeFriendFrom(UserEntity from) {
        if(this.friendsFrom == null) this.friendsFrom = new ArrayList<>();
        this.friendsFrom.remove(from);
    }

}
