package toyProject.snow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID memberUUID;

    private MemberType memberType;

//    @Column(nullable = false)
    private String name;
//    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private Timestamp joinDate;

}
