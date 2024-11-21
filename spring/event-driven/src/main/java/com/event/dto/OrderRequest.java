package com.event.dto;

import lombok.Builder;

public class OrderRequest {

    private String name;
    private int quantity;
    private int price;

    @Builder
    public OrderRequest(String name, int quantity, int price, Long memberId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderRequest of(String name, int quantity, int price, Long memberId) {
        return OrderRequest.builder()
                .name(name)
                .quantity(quantity)
                .price(price)
                .build();
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
