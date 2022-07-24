package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService service;
    @Autowired
    MemberRepository repo;

    @Test
    public void 회원가입() throws Exception {
        //이런게 주어졌을 때 :: given
        Member member = new Member();
        member.setName("Jeong");

        //이걸 실행하면 :: when
        Long savedId = service.join(member);
        
        //결과가 나와야 됨 :: then
        assertEquals(member, repo.findOne(savedId));
        // Assert.assertEquals(); <<-- 이것을 사용함
        
    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //이런게 주어졌을 때 :: given
        Member member1 = new Member();
        member1.setName("Jeong");

        Member member2 = new Member();
        member2.setName("Jeong");

        //이걸 실행하면 :: when
        service.join(member1);
        service.join(member2);

        /*
        service.join(member1);
        try {
            service.join(member2); //예외가 발생해야함.
        }catch (IllegalStateException e){
            return;
        }
        @Test(expected = IllegalStateException.class)
        */

        //결과가 나와야 됨 :: then
        Assert.fail("예외가 발생해야함 :: 이게 보이면 안됌");
    }
}