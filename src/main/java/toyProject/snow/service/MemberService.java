package toyProject.snow.service;

import java.sql.Timestamp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyProject.snow.dto.member.memberResponse.MemberInfoResponse;
import toyProject.snow.entity.MemberEntity;
import toyProject.snow.jwt.JWTUtil;
import toyProject.snow.repository.MemberRepository;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID memberUUID) {
        // 완전 전통적인 방식 vs record 방식.... 헹 너무 난이도 올리기 싫은데



        String name = memberRepository.findByMemberUUID(memberUUID).getName();
        String nickname = memberRepository.findByMemberUUID(memberUUID).getNickname();
        String email = memberRepository.findByMemberUUID(memberUUID).getEmail();
        Timestamp joinDate = Timestamp.valueOf(memberRepository.findByMemberUUID(memberUUID).getJoinDate().toLocalDateTime());

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(name, nickname, email, joinDate);

        return memberInfoResponse;
    }

    @Transactional
    public void deleteMember(UUID memberUUID) {
        if (!memberRepository.existsById(memberUUID)){

        }
        memberRepository.deleteByMemberUUID(memberUUID);
    }

    @Transactional
    public MemberEntity updateMember(UUID memberUUID) {
        return memberRepository.findByMemberUUID(memberUUID);
    }
}
