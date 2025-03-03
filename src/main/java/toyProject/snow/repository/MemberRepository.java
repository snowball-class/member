package toyProject.snow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyProject.snow.entity.member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<member, UUID> {

    boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    boolean existsByMemberUUID(UUID memberUUID);
    member findByEmail(String email);
//    MemberEntity findByMemberUUID(UUID memberUUID);
    void deleteByMemberUUID(UUID memberUUID);

    Optional<member> findByMemberUUID(UUID memberUUID);

}
