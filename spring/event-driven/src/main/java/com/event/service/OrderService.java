package com.event.service;

import com.event.annotation.Transactional;
import com.event.domain.Member;
import com.event.domain.Order;
import com.event.dto.OrderRequest;
import com.event.listener.event.OrderEvent;
import com.event.repository.MemberRepository;
import com.event.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void create(OrderRequest orderRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Order order = Order.createOrder(orderRequest, member);
        orderRepository.save(order);

        eventPublisher.publishEvent(OrderEvent.of(order));
    }
}
