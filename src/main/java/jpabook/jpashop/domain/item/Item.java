package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    /**
     * 비즈니스 로직을 여기에 만든 이유는 stockQuantity Entity가
     * 해당 클래스에 있기때문에 만듦 :: 이렇게 해야지 유지보수가 쉽고 응집력이 강해짐.
     */

    //== 비즈니스 로직 ==//
    /**
    * Stock 증가
    */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * Stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }


}
