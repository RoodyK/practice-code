package com.event.domain;

import com.event.dto.OrderRequest;
import lombok.Builder;

public class Order {

    private String productName;
    private int quantity;
    private int price;
    private Member member;

    @Builder
    private Order(String productName, int quantity, int price, Member member) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.member = member;
    }

    public static Order createOrder(OrderRequest request, Member member) {
        return Order.builder()
                .productName(request.getName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .member(member)
                .build();
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public Member getMember() {
        return member;
    }

    public String getMemberName() {
        return member.getName();
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", member=" + member +
                '}';
    }
}
