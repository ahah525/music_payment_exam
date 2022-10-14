package com.ll.exam.app__2022_10_11.app.order.service;

import com.ll.exam.app__2022_10_11.app.cart.entity.CartItem;
import com.ll.exam.app__2022_10_11.app.cart.service.CartService;
import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import com.ll.exam.app__2022_10_11.app.member.service.MemberService;
import com.ll.exam.app__2022_10_11.app.order.entity.Order;
import com.ll.exam.app__2022_10_11.app.order.entity.OrderItem;
import com.ll.exam.app__2022_10_11.app.order.repository.OrderItemRepository;
import com.ll.exam.app__2022_10_11.app.order.repository.OrderRepository;
import com.ll.exam.app__2022_10_11.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CartService cartService;
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // 장바구니로부터 주문 생성
    @Transactional
    public Order createFromCart(Member buyer) {
        List<CartItem> cartItems = cartService.getItemsByBuyer(buyer);
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            // 주문 가능하다면 주문에 상품 추가
            if(product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }
            // 장바구니에서 상품 삭제
            cartService.removeItem(buyer, product);
        }

        return create(buyer, orderItems);
    }

    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .buyer(buyer)
                .build();

        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.makeName();

        orderRepository.save(order);

        return order;
    }

    // 예치금으로 전액 결제
    @Transactional
    public void payByRestCashOnly(Order order) {
        Member buyer = order.getBuyer();    // 구매자
        long restCash = buyer.getRestCash();    // 예치금 잔액
        int payPrice = order.calculatePayPrice();     // 결제 금액

        // 예치금 잔액 < 결제 금액 이면, 결제 거절
        if(restCash < payPrice) {
            throw new RuntimeException("예치금이 부족합니다.");
        }
        // 예치금 차감 처리
        memberService.addCash(buyer, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        // 결제 완료 처리
        order.setPaymentDone();
        orderRepository.save(order);
    }

    // 전액 환불
    @Transactional
    public void refund(Order order) {
        Member buyer = order.getBuyer();    // 구매자
        int payPrice = order.getPayPrice();     // 결제 금액

        // 예치금 환불 처리
        memberService.addCash(order.getBuyer(), payPrice, "주문__%d__환불__예치금".formatted(order.getId()));

        // 환불 처리
        order.setRefundDone();
        orderRepository.save(order);
    }

    public Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    // 화면 표시용 주문 조회
    public Optional<Order> findForPrintById(long id) {
        return findById(id);
    }

    // 주문 조회 권한 체크
    public boolean actorCanSee(Member actor, Order order) {
        return actor.getId().equals(order.getBuyer().getId());
    }

    // TossPayments 결제(전액 결제 or 혼합 결제)
    @Transactional
    public void payByTossPayments(Order order, long useRestCash) {
        Member buyer = order.getBuyer();              // 구매자
        int payPrice = order.calculatePayPrice();     // 결제 금액
        long pgPayPrice = payPrice - useRestCash;     // 카드 결제 금액

        // 카드 결제 예치금 처리
        memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        // 사용된 예치금 처리
        if(useRestCash > 0) {
            memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        }

        // 결제 완료 처리
        order.setPaymentDone();
        orderRepository.save(order);
    }

    public boolean actorCanPayment(Member actor, Order order) {
        return actorCanSee(actor, order);
    }

    // 해당 날짜 범위의 OrderItem 조회
    public List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }
}
