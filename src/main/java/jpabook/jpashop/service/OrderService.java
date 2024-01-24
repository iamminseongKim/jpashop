package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     *
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성 > 생성 매서드 !!
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성 > 생성 매서드 !!
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장 >> order 만 저장하는게 Order 에서 OrderItem, Delivery 는 CascadeType.ALL 옵션이 있어서 바로 같이 저장됨.
        orderRepository.save(order);
        return order.getId();

    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소 > 취소 메서드를 만들어놨고 order 를 선언할 때 정보가 다 샛팅 된 것
        order.cancel();
        // 캔슬 함수를 보면 오더 상태를 바꾸고, orderItem 에 재고를 바꾸기 때문에
        // jpa 에서 상태를 감지해서 db 에 알아서 update 를 날리게 된다.!

    }

    // 검색
    public List<Order> findOrder(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
