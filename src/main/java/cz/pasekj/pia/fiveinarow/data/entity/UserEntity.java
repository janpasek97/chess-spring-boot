package cz.pasekj.pia.fiveinarow.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entity representing a user
 * Stored in Postgres DB
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** username */
    @Column(nullable = false, length = 20)
    private String username;

    /** email of the user - not changeable */
    @Column(nullable = false, length = 50)
    private String email;

    /** user password */
    @Column(nullable = false, length = 60)
    private String password;

    /** enabled flag - at the moment used as true=legacy user, false=OAuth2 user */
    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    /** List of user who requested/confirmed friendship */
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "friends_list",
            joinColumns = @JoinColumn(name = "from_user_fk", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "to_user_fk", referencedColumnName = "id")
    )
    private List<UserEntity> friendsFrom;

    /** List of user who this user asked for a friendship */
    @ManyToMany(mappedBy = "friendsFrom")
    private List<UserEntity> friendsTo;

    /** List of roles associated with the user */
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_has_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<RoleEntity> roles;

    /** List of won matches */
    @OneToMany(mappedBy = "winner")
    private List<GameResultEntity> winnings;

    /** List of lost matches */
    @OneToMany(mappedBy = "loser")
    private List<GameResultEntity> losses;

    /**
     * Constructor - used by framework
     */
    public UserEntity() { }

    /**
     * Constructor
     * @param username username
     * @param email email
     * @param password password (shall be encrypted)
     * @param enabled flag with semantic, true=legacy user/false=OAuth2 user
     */
    public UserEntity(String username, String email, String password, boolean enabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    /**
     * Get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get password (shall be encrypted)
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get info if the user is enabled
     * @return get enabled flag
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get List of users that the current user is a friend to
     * @return List of users
     */
    public List<UserEntity> getFriendTo() {
        return friendsTo;
    }

    /**
     * Get List of users that the current user is a friend from
     * @return List of users
     */
    public List<UserEntity> getFriendFrom() {
        return friendsFrom;
    }

    /**
     * Get roles of the user
     * @return list of roles
     */
    public List<RoleEntity> getRoles() {
        return roles;
    }

    /**
     * Get won matches
     * @return list of game results
     */
    public List<GameResultEntity> getWinnings() {
        return winnings;
    }

    /**
     * Get lost matches
     * @return list of game results
     */
    public List<GameResultEntity> getLosses() {
        return losses;
    }

    /**
     * Set username
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set password
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Clear user's roles
     */
    public void clearRoles() {
        if(this.roles == null) this.roles = new ArrayList<>();
        this.roles.clear();
    }

    /**
     * Add a roles to the user
     * @param roles roles to add
     */
    public void addRole(RoleEntity... roles) {
        if(this.roles == null) this.roles = new ArrayList<>();
        this.roles.addAll(Arrays.asList(roles));
    }

    /**
     * Add a friend to
     * @param to user that is friend to
     */
    public void addFriendTo(UserEntity to) {
        if(this.friendsTo == null) this.friendsTo = new ArrayList<>();
        this.friendsTo.add(to);
    }

    /**
     * Add a friend from
     * @param from user that is friend from
     */
    public void addFriendFrom(UserEntity from) {
        if(this.friendsFrom == null) this.friendsFrom = new ArrayList<>();
        this.friendsFrom.add(from);
    }

    /**
     * Remove given friend to
     * @param to user that is friend to
     */
    public void removeFriendTo(UserEntity to) {
        if(this.friendsTo == null) this.friendsTo = new ArrayList<>();
        this.friendsTo.remove(to);
    }

    /**
     * Remove given friend from
     * @param from user that is friend from
     */
    public void removeFriendFrom(UserEntity from) {
        if(this.friendsFrom == null) this.friendsFrom = new ArrayList<>();
        this.friendsFrom.remove(from);
    }

}
