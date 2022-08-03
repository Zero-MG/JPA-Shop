package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        /**
         * id값이 null이라면 새로 INSERT 하고,
         * id값이 null이 아니고 값이 있다면, merge는 교체 해줌
         */
        if (item.getId() == null) {
            em.persist(item);
        }else{
            em.merge(item); //약간 세이브? 업데이트? 리로딩? 그런 느낌?
            // 병합. 실무에서 사용하지 않지만, 설명을 위해 씀.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
    }
}
