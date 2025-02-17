//package toyProject.snow.service;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import toyProject.snow.entity.MemberEntity;
//import toyProject.snow.repository.MemberRepository;
//
//import java.util.UUID;
//
//@Service
//public class MemberService {
//
//    private MemberRepository memberRepository;
//
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }
//
//    @Transactional
//    public MemberEntity getMemberInfo(UUID memberUUID) {
//        return memberRepository.findByMemberUUID(memberUUID);
//    }
//
//    @Transactional
//    public void deleteMember(UUID memberUUID) {
//        if (!memberRepository.existsById(memberUUID)){
//
//        }
//        memberRepository.deleteByUUID(memberUUID);
//    }
//
//    @Transactional
//    public MemberEntity updateMember(UUID memberUUID) {
//        return memberRepository.findByMemberUUID(memberUUID);
//    }
//}
