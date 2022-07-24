package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    /*
    * @PersistenceContext :: 어노테이션을 사용하면 순수하게
    * 매니저 팩토리에 접근해서 가져올 필요 없이 가져와서 사용할 수 있다.
    * Tip1. Spring 최신버전에서 @Autowired도, @PersistenceContext 대신 사용가능하게 지원한다.
    * Tip2. 위와 같은 내용으로 인해 @RequiredArgsConstructor을 사용하여 필드에 final 추가 후 사용도 가능하다.
    *    :: Service 단 어노테이션 구조 참고 ::
    */
    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); //단일 조회
    }

    public List<Member> findAll() {
        // JPQL - 일반 쿼리문과 다르게 JPQL은 Entity객체를 대상으로 조회함.
        // MySql같은 경우는 테이블을 대상으로 하지만 JPQL은 조금 다르다. ------------ ★필독★
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        // JPQL 파라미터를 넣어서 Query문 작성하는 방법
        return em.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
