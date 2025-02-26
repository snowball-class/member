package toyProject.snow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyProject.snow.entity.MemberEntity;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {

    boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    boolean existsByMemberUUID(UUID memberUUID);
    MemberEntity findByEmail(String email);
//    MemberEntity findByMemberUUID(UUID memberUUID);
    void deleteByMemberUUID(UUID memberUUID);

    Optional<MemberEntity> findByMemberUUID(UUID memberUUID);

}
