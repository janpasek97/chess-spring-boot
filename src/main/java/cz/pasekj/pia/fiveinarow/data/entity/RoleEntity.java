package cz.pasekj.pia.fiveinarow.data.entity;

import javax.persistence.*;

/**
 * Entity representing a role
 * Stored in Postgres DB
 */
@Entity
@Table(name = "user_roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Name of the role (eg. ROLE_USER, ROLE_ADMIN) */
    @Column
    private String name;

    /**
     * Constructor - used by the framework
     */
    public RoleEntity() { }

    /**
     * Constructor
     * @param name name of the role
     */
    public RoleEntity(String name) {
        this.name = name;
    }

    /**
     * Get name of the role
     * @return name of the role
     */
    public String getName() {
        return name;
    }
}
