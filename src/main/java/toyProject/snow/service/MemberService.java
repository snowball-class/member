package toyProject.snow.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyProject.snow.dto.member.memberRequest.MemberUpdateRequest;
import toyProject.snow.dto.member.memberResponse.MemberDeletetResponse;
import toyProject.snow.dto.member.memberResponse.MemberInfoResponse;
import toyProject.snow.dto.member.memberResponse.MemberUpdateResponse;
import toyProject.snow.entity.Member;
import toyProject.snow.handler.ExceptionResponseHandler;
import toyProject.snow.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID memberUUID) {
        // 완전 전통적인 방식 vs record 방식.... 헹 너무 난이도 올리기 싫은데

        Optional<Member> optionalMemberEntity = memberRepository.findByMemberUUID(memberUUID);
        if(!optionalMemberEntity.isPresent()){
            return new MemberInfoResponse(false);
        }

        Member member = optionalMemberEntity.get();

        String name = member.getName();
        String nickname = member.getNickname();
        String email = member.getEmail();
        LocalDateTime joinDate = member.getJoinDate();

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(true, name, nickname, email, joinDate);

        return memberInfoResponse;
    }

    @Transactional
    public MemberDeletetResponse deleteMember(UUID memberUUID) {
        if (!memberRepository.existsByMemberUUID(memberUUID)){
            return new MemberDeletetResponse(false);
        }
        memberRepository.deleteByMemberUUID(memberUUID);
        return new MemberDeletetResponse(true);
    }

    @Transactional
    public MemberUpdateResponse updateMember(UUID memberUUID, MemberUpdateRequest request) {

        Optional<Member> optionalMemberEntity = memberRepository.findByMemberUUID(memberUUID);

//        MemberEntity memberEntity = memberRepository.findByMemberUUID(memberUUID)
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        if(!optionalMemberEntity.isPresent()){
            return new MemberUpdateResponse(false);
        }

        Member member = optionalMemberEntity.get();

        if(!bCryptPasswordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new ExceptionResponseHandler.passwordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        member.setNickname(request.getNewNickname());
        member.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

        return new MemberUpdateResponse(true, request.getNewNickname());
    }
}
