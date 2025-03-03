package toyProject.snow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import toyProject.snow.entity.refresh_token;

public interface RefreshTokenRepository extends JpaRepository<refresh_token, Long> {

    Boolean existsByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refresh);
}
