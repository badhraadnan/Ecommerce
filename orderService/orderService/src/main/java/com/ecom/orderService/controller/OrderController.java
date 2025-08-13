package com.ecom.orderService.controller;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.orderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order/service")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseModel addOrder(@RequestParam Long userId,@RequestParam long addressId){
        return orderService.addOrder(userId,addressId);
    }

    @GetMapping("/get/{userId}")
    public ResponseModel getUserOrders(@PathVariable Long userId) {
        return  orderService.getOrdersByUserId(userId);
    }

    @PatchMapping("/update-status/")
    public ResponseModel updateStatus(@RequestParam Long orderId, @RequestParam OrderStatus orderStatus){
        return orderService.updateStatus(orderId,orderStatus);
    }
    @PatchMapping("/cancel-order/{orderId}")
    public ResponseModel cancelOrder(@PathVariable Long orderId){
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/all-orders")
    public ResponseModel getAllOrders(){
        return orderService.GetAllOrders();
    }

    @PatchMapping("/return-order/{orderId}")
    public ResponseModel returnOrder(@PathVariable Long orderId){
        return orderService.returnOrder(orderId);
    }
}
