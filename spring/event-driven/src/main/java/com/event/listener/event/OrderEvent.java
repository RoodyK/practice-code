package com.event.listener.event;

import com.event.domain.Order;
import lombok.Builder;

public class OrderEvent {
    private String memberName;
    private String productName;

    @Builder
    public OrderEvent(String memberName, String productName) {
        this.memberName = memberName;
        this.productName = productName;
    }

    public static OrderEvent of(Order order) {
        return OrderEvent.builder()
                .memberName(order.getMemberName())
                .productName(order.getProductName())
                .build();
    }

    public String getMemberName() {
        return memberName;
    }

    public String getProductName() {
        return productName;
    }
}
