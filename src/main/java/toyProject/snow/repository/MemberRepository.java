package toyProject.snow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyProject.snow.entity.MemberEntity;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {

//    @Override
//    boolean existsById(UUID uuid);

    boolean existsByEmail(String email);
    boolean existBymemberUUID(UUID memberUUID);
    MemberEntity findByEmail(String email);
    MemberEntity findByMemberUUID(UUID memberUUID);
    void deleteByUUID(UUID memberUUID);
}
