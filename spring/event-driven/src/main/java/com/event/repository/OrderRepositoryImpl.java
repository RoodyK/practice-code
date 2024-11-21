package com.event.repository;

import com.event.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class OrderRepositoryImpl implements OrderRepository{

    private static final Map<String, Order> store = new HashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.getProductName(), order);
        log.info("주문을 저장했습니다.");
    }
}
