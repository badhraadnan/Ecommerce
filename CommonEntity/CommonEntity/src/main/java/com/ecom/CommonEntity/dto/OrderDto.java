package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.entity.Address;
import com.ecom.CommonEntity.entity.OrderItem;
import com.ecom.CommonEntity.entity.Orders;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private Long userId;
    private String firstName;
    private String email;
    private String mobile;
    private String shortAddress;
    private String countryName;
    private String stateName;
    private String cityName;
    private Long zipCode;
    private Long orderItems;
    private String name;
    private String imageURL;
    private Double price;
    private int qty;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime createAt;

    public static OrderDto getOrder(OrderItem orders) {
        return OrderDto.builder()
                .orderId(orders.getOrders().getOrderId())
                .userId(orders.getOrders().getUser().getUserId())
                .firstName(orders.getOrders().getUser().getFirstName())
                .email(orders.getOrders().getUser().getEmail())
                .mobile(orders.getOrders().getUser().getMobile())
                .shortAddress(orders.getOrders().getAddress().getShortAddress())
                .countryName(orders.getOrders().getAddress().getCountry().getCountryName())
                .stateName(orders.getOrders().getAddress().getState().getStateName())
                .cityName(orders.getOrders().getAddress().getCity().getCityName())
                .zipCode(orders.getOrders().getAddress().getZipCode())
                .orderItems(orders.getOrderItemId())
                .name(orders.getProduct().getName())
                .imageURL(orders.getProduct().getImageURL())
                .qty(orders.getQuantity())
                .price(orders.getPrice())
                .totalAmount(orders.getOrders().getTotalAmount())
                .orderStatus(orders.getOrders().getOrderStatus())
                .orderStatus(orders.getOrderStatus())
                .createAt(orders.getCreatedAt())
                .build();
    }



}