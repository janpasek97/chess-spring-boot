package cz.pasekj.pia.fiveinarow.data.entity;

import javax.persistence.*;

@Entity
@Table( name = "friends_list",
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_user_fk", "to_user_fk"}))
public class FriendsListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_fk")
    private UserEntity userFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_fk")
    private UserEntity userTo;

    public FriendsListEntity() { }

    public FriendsListEntity(UserEntity userFrom, UserEntity userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }
}
