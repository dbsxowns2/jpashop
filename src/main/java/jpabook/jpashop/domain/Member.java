package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded  // 내장 타입
    private Address address;

    @OneToMany(mappedBy = "member") // Orders 테이블에 있는 Member field 에 의해 맵핑된 연관관계 거울임을 명시!
    private List<Order> orders = new ArrayList<>();

}
