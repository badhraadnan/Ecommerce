package com.ecom.orderService.service;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.model.ResponseModel;

import java.util.List;

public interface OrderService {
    ResponseModel addOrder(Long userId,long addressID);

    ResponseModel getOrdersByUserId(Long userId);

    ResponseModel updateStatus(Long orderId, OrderStatus orderStatus);

    ResponseModel cancelOrder(Long orderId);

    ResponseModel GetAllOrders();

    ResponseModel returnOrder(Long orderId);



}
