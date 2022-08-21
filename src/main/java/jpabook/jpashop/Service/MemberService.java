package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //트랜잭션 Spring 어노테이션 사용하기.
@RequiredArgsConstructor //final이 걸려있는 필드에 생성자를 만들어줌.
public class MemberService {

    /*
    * 필드에 @Autowired를 주입하지 말고 생성자를 만들어서 주입하자.
    *  Tip1. 필드가 1개일 경우 생성자에 @Autowired를 쓰지 않아도 Spring에서 자동으로 주입해줌.
    *  Tip2. 필드가 변경되지 않을 거라면 final을 걸어주는 것이 좋음.
    */
    private final MemberRepository repo;

//    @Autowired
//    public MemberService(MemberRepository repo) {
//        this.repo = repo;
//    }

    /*
        회원가입
    */
    // @Transactional(readOnly = true) :: 읽기전용으로만 사용 (수정, 삭제, 삽입 안됨)
    // def값은 false 이므로 그냥 트랜잭션 걸어주기.
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복회원 검증
        repo.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = repo.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    
    //회원 전체조회
    public List<Member> findMembers() {
        return repo.findAll();
    }

    public Member findOne(Long memberId) {
        return repo.findOne(memberId); //단건조회
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = repo.findOne(id);
        member.setName(name); // 트랜젝션 내 Entity가 변경된 것을 JPA가 감지하여 커밋해줌.
        // [변경감지] 여기서 트랜젝션 커밋이 발생하면 DB에 수정이 반영됨.
    }
}
