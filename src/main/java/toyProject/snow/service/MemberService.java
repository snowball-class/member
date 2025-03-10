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

        Optional<Member> optionalMemberEntity = memberRepository.findByMemberUUID(memberUUID);
        if(!optionalMemberEntity.isPresent()){
            return new MemberInfoResponse(false);
        }

        Member member = optionalMemberEntity.get();

        String name = member.getName();
        String nickname = member.getNickname();
        String email = member.getEmail();
        LocalDateTime joinDate = member.getJoinDate();

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(true, memberUUID, name, nickname, email, joinDate);

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

        Optional<Member> optionalMember = memberRepository.findByMemberUUID(memberUUID);

        if(!optionalMember.isPresent()){
            return new MemberUpdateResponse(false);
        }

        Member member = optionalMember.get();

        if(!bCryptPasswordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new ExceptionResponseHandler.PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        member.setNickname(request.getNewNickname());
        member.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

        return new MemberUpdateResponse(true, request.getNewNickname());
    }
}
