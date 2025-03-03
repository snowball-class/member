package toyProject.snow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class refresh_token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID memberUUID;
    @Column(name = "refreshToken", columnDefinition = "TEXT")
    private String refreshToken;
    private String expirationTime;
}
