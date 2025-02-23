package toyProject.snow.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyProject.snow.dto.join.joinRequest.JoinRequest;
import toyProject.snow.dto.join.joinResponse.JoinResponse;
import toyProject.snow.entity.MemberEntity;
import toyProject.snow.entity.MemberType;
import toyProject.snow.repository.MemberRepository;

@Service
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // boolean으로 회원가입 성공하면 true, 실패하면 false go
    @Transactional
    public JoinResponse join(JoinRequest joinRequest){

        String name = joinRequest.getName();
        String nickname = joinRequest.getNickname();
        String email = joinRequest.getEmail();
        String password = joinRequest.getPassword();

        Boolean isExist = memberRepository.existsByEmail(email);

        if(isExist){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        MemberEntity newMember = new MemberEntity();
        newMember.setName(name);
        newMember.setNickname(nickname);
        newMember.setEmail(email);
        newMember.setMemberType(MemberType.ROLE_MEMBER);
        newMember.setPassword(bCryptPasswordEncoder.encode(password));

        memberRepository.save(newMember);

        return new JoinResponse(newMember.getName(), newMember.getNickname(), newMember.getMemberType());
    }
}
