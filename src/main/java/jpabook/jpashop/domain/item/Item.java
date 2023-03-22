package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item { // 상속관계 전략을 부모클래스에 지정!

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // === 비지니스 로직 === //
    // └─▶ 이전엔 itemService 에서 stockQuantity를 가져와 비지니스 로직을 수행하고 item.setQuantity 를 사용해 데이터를 갱신하였음
    // └─▶ 객체지향적으로 생각했을 때, Data를 가지고 있는 곳에서 비지니스 로직를 수행하는게 응집도가 높다!
    // └─▶ 따라서, Setter 없이 내부 메서드를 사용해서 관리하자!

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("Need More Stock");
        }
        this.stockQuantity = restStock;
    }

}
