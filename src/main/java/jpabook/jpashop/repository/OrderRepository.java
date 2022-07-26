package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /** ================================================================================== **/
    /**
     * 데이터가 다 있다면 (null 값이 없다면) 아래 쿼리로 실행 가능
     */

    public List<Order> findAll(OrderSearch orderSearch) {

        return em.createQuery("SELECT o from Order o join o.member m", Order.class) // SQL Query Join 문
                .setMaxResults(1000) //데이터를 불러오는 제한 갯수 (1000개를 불러옴) :: 최대 1000건
                .getResultList(); //쿼리 결과 얻기
    }
/* 참고 쿼리
        return em.createQuery("SELECT o from Order o join o.member m" +
                " WHERE o.status = :status and m.name like :name", Order.class) // SQL Query Join 문
                .setParameter("status", orderSearch.getOrderStatus()) //파라미터 받아오기
                .setParameter("name", orderSearch.getMemberName()) //파라미터 받아오기
                .setFirstResult(100) //페이징처리 (100부터 시작해서)
                .setMaxResults(1000) //데이터를 불러오는 제한 갯수 (1000개를 불러옴) :: 최대 1000건
                .getResultList(); //쿼리 결과 얻기
*/

    /** ================================================================================== **/
    /**
     * String으로 동적쿼리 사용...
     */

    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /** ================================================================================== **/
    /**
     * JPA Criteria로 동적쿼리 사용
     */

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }


    /**
     * 결론 .. 은..
     * Query DSL을 써라......
     */


}
