package toyProject.snow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID memberUUID;
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;
    @Column(nullable = false)
    private String expirationTime;
}
