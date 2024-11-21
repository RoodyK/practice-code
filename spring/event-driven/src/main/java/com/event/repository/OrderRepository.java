package com.event.repository;

import com.event.domain.Order;

public interface OrderRepository {

    void save(Order order);
}
