package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // Cascade 의 범위 참조하는 주인이 private Owner 인 경우 포함한다.
        
        // Entity 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // 주문을 저장하면 Cascade 덕분에 OrderItem 과 Delivery 는 자동으로 함께 DB에 Insert 된다.

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();
        /* "Transactional Script Pattern"
        * 일반적으로 DB를 직접 다루는 library 를 사용할 경우, 데이터를 변경한다음 Service/Controller 에서 Update 쿼리를 직접 작성해 Repository 에 넘겨주어야한다.
        * Service 계층에서 비지니스로직을 전부 작성해야한다.
        *
        *  JPA
        * Entity 의 Data 만 바꿔주어도 JPA 가 알아서 바뀐 부분을 감지한다. ( Dirty Checking ) 변경된 내역을 찾아 DB에 Update 쿼리를 보내준다.
        * Entity 에 핵심 비지니스 로직을 몰아 넣는 것을 "도메인 모델 패턴" 이라한다.
        * */
    }

    /**
     * 검색
     */
    /*public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }*/
}
