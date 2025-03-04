package toyProject.snow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyProject.snow.entity.Member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    boolean existsByMemberUUID(UUID memberUUID);
    Member findByEmail(String email);
//    MemberEntity findByMemberUUID(UUID memberUUID);
    void deleteByMemberUUID(UUID memberUUID);

    Optional<Member> findByMemberUUID(UUID memberUUID);

}
