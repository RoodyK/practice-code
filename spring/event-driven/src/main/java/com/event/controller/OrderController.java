package com.event.controller;

import com.event.dto.OrderRequest;
import com.event.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 예제의 편의를 위해서 get 메서드 사용
    @GetMapping("/orders")
    public String createOrder() {
        OrderRequest request = OrderRequest.of("상품1", 1, 10000, 1L);
        Long memberId = 1L;

        orderService.create(request, memberId);

        return "ok";
    }
}
