package toyProject.snow.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyProject.snow.dto.member.memberRequest.MemberUpdateRequest;
import toyProject.snow.dto.member.memberResponse.GenerateTempPasswordResponse;
import toyProject.snow.dto.member.memberResponse.MemberDeletetResponse;
import toyProject.snow.dto.member.memberResponse.MemberInfoResponse;
import toyProject.snow.dto.member.memberResponse.MemberUpdateResponse;
import toyProject.snow.entity.Member;
import toyProject.snow.handler.ExceptionResponseHandler;
import toyProject.snow.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService){
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID memberUUID) {

        Optional<Member> optionalMember = memberRepository.findByMemberUUID(memberUUID);
        if(!optionalMember.isPresent()){
            return new MemberInfoResponse(false);
        }

        Member member = optionalMember.orElseThrow(()
                -> new NoSuchElementException());

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

        Member member = optionalMember.orElseThrow(()
                -> new NoSuchElementException());

        if(!bCryptPasswordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new ExceptionResponseHandler.PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        member.setNickname(request.getNewNickname());
        member.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

        return new MemberUpdateResponse(true, request.getNewNickname());
    }

    @Transactional
    public GenerateTempPasswordResponse generateTemporaryPassword(UUID memberUUID) {

        if(!memberRepository.existsByMemberUUID(memberUUID)){
            throw new NoSuchElementException();
        }

        Optional<Member> optionalMember = memberRepository.findByMemberUUID(memberUUID);
        Member member = optionalMember.orElseThrow(() -> new NoSuchElementException());
        String email = member.getEmail();

        // UUID로 임시 비밀번호 생성
        String temporaryPassword = generateRandomPassword(10);

        // db에 임시 비밀번호 bCrypt 처리해 저장
        member.setPassword(bCryptPasswordEncoder.encode(temporaryPassword));
        memberRepository.save(member);

        // 이메일로 임시 비밀번호 전송
        emailService.sendTemporaryPasswordByEmail(email, temporaryPassword);

        return new GenerateTempPasswordResponse(true);
    }

    public String generateRandomPassword(int length){
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }
}
