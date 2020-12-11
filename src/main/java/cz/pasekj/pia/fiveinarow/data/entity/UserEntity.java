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

    @OneToMany(mappedBy = "userFrom")
    private List<FriendsListEntity> friendTo;

    @OneToMany(mappedBy = "userTo")
    private List<FriendsListEntity> friendFrom;

    @ManyToMany
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

    public List<FriendsListEntity> getFriendTo() {
        return friendTo;
    }

    public List<FriendsListEntity> getFriendFrom() {
        return friendFrom;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void addRole(RoleEntity... roles) {
        if(this.roles == null) this.roles = new ArrayList<>();
        this.roles.addAll(Arrays.asList(roles));
    }
}
