package cz.pasekj.pia.fiveinarow.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    public RoleEntity() { }

    public RoleEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
