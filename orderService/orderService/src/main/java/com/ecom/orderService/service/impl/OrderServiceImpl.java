package com.ecom.orderService.service.impl;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.OrderDto;
import com.ecom.CommonEntity.entity.*;
import com.ecom.CommonEntity.model.ErrorMsg;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.SuccessMsg;
import com.ecom.commonRepository.dao.MasterDao;
import com.ecom.orderService.config.RabbitMQConfig;
import com.ecom.orderService.exception.*;
import com.ecom.orderService.service.OrderService;
import com.ecom.orderService.utils.RabbitMQProducer;
import com.ecom.orderService.utils.emailSend;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MasterDao masterDao;

    @Autowired
    private emailSend emailSend;

    @Autowired
    private RabbitMQProducer producer;

    @Autowired
    private ObjectMapper objectMapper;


    //Place Order --User Side
    @Override
    @Transactional
    public ResponseModel addOrder(Long userId, long addressId) {
        try {
            Address address = masterDao.getAddressRepo().findByUser_UserIdAndAddressId(userId, addressId)
                    .orElseThrow(() -> new AddressNotFound(ErrorMsg.ADDRESS_NOT_FOUND));
            User user = masterDao.getUserRepository().findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(ErrorMsg.USER_NOT_FOUND));


            Cart cart = masterDao.getCartRepo().getCartByUserId(user.getUserId())
                    .orElseThrow(() -> new CartNotFound(ErrorMsg.CART_NOT_FOUND));

            Orders orders = Orders.builder()
                    .address(address)
                    .user(user)
                    .totalAmount(cart.getTotalAmount())
                    .orderStatus(OrderStatus.PLACED)
                    .build();

            masterDao.getOrderRepo().save(orders);

            List<CartItem> cartItems = masterDao.getCartItemsRepo().findCartByCartId(cart.getCartId());
            List<OrderItem> itemList = new ArrayList<>();
            List<Product> updatedProducts = new ArrayList<>();

            for (CartItem item : cartItems) {
                Product product = masterDao.getProductRepo()
                        .findByproductIDAndStatus(item.getProduct().getProductID(), Status.ACTIVE)
                        .orElseThrow(() -> new ProductNotFound(ErrorMsg.PRODUCT_NOT_FOUND));

                if (product.getQty() < item.getQuantity()) {
                    return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.PRODUCT_OUT_OF_STOCK);
                }

                OrderItem orderItem = OrderItem.builder()
                        .orders(orders)
                        .product(product)
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .orderStatus(OrderStatus.PLACED)
                        .build();

                itemList.add(orderItem);

                product.setQty(product.getQty() - item.getQuantity());
                updatedProducts.add(product);
            }

            masterDao.getOrderItemsRepo().saveAll(itemList);
            masterDao.getProductRepo().saveAll(updatedProducts);

//             Optionally delete or mark cart inactive
            masterDao.getCartItemsRepo().deleteAll(cartItems);
            masterDao.getCartRepo().delete(cart);

            List<OrderDto> dtos = itemList.stream()
                    .map(OrderDto::getOrder)
                    .toList();

            List<Map<String, String>> productList = dtos.stream().map(
                    dto -> Map.of(
                            "OrderId", String.valueOf(dto.getOrderId()),
                            "ProductName", dto.getName(),
                            "Quantity", String.valueOf(dto.getQty()),
                            "Price", String.valueOf(dto.getTotalAmount()),
                            "Status", dto.getOrderStatus().toString(),
                            "Image", dto.getImageURL(),
                            "Qty", String.valueOf(dto.getQty())
                    )).toList();


            Map<String, Object> map = Map.of(
                    "UserName", user.getFirstName() + " " + user.getLastName(),
                    "Email", user.getEmail(),
                    "Products", productList
            );


            producer.sendMessage(objectMapper.writeValueAsString(map),
                    RabbitMQConfig.ECOMMERCE_EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY_ORDER_PROCESSOR);


            return new ResponseModel(HttpStatus.OK, dtos, SuccessMsg.ORDER_PLACED);
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR+"\n"+e.getMessage());
        }
    }


    //Get All Order By Each User --User Side
    @Override
    public ResponseModel getOrdersByUserId(Long userId) {
        List<OrderDto> orders = masterDao.getOrderItemsRepo().findUserIdAndStatus(userId, Status.ACTIVE)
                .stream()
                .map(OrderDto::getOrder)
                .toList();

        if (orders.isEmpty()) {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.ORDER_NOT_FOUND);
        }
        return new ResponseModel(HttpStatus.OK, orders, SuccessMsg.ORDER_FETCHED_SUCCESSFULLY);
    }

    //Order Status Update --Admin Side
    @Override
    public ResponseModel updateStatus(Long orderId, OrderStatus orderStatus) {
        try {
            OrderItem existOrder = masterDao.getOrderItemsRepo().findById(orderId).orElse(null);

            if (existOrder.getOrderStatus() == orderStatus) {
                return new ResponseModel(HttpStatus.NOT_ACCEPTABLE, null, orderStatus + " Already Applied");
            }
            existOrder.setOrderStatus(orderStatus);
            existOrder.setUpdatedAt(LocalDateTime.now());
            existOrder.getOrders().setUpdatedAt(LocalDateTime.now());
            OrderItem saveItem = masterDao.getOrderItemsRepo().save(existOrder);
            return new ResponseModel(HttpStatus.OK, OrderDto.getOrder(saveItem), "Status change Successfully  ");
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "something went to wrong");
        }
    }

    //Cancel Order --User Side
    @Override
    public ResponseModel cancelOrder(Long orderId) {
        try {
            OrderItem orders = masterDao.getOrderItemsRepo().findById(orderId).orElseThrow(() -> new OrderNotFound(ErrorMsg.ORDER_NOT_FOUND));

            if (orders.getOrderStatus() == OrderStatus.PLACED || orders.getOrderStatus() == OrderStatus.PROCESSING) {
                orders.setOrderStatus(OrderStatus.CANCELLED);
                orders.setUpdatedAt(LocalDateTime.now());

                double oldTotal = orders.getOrders().getTotalAmount();
                double unitPrice = orders.getPrice();

                double updatedPrice = oldTotal - unitPrice;
                orders.getOrders().setTotalAmount(updatedPrice);

                OrderItem saveOrder = masterDao.getOrderItemsRepo().save(orders);

                Map<String, String> map = Map.of(
                        "userName", saveOrder.getOrders().getUser().getFirstName() + "  " + saveOrder.getOrders().getUser().getLastName(),
                        "email", saveOrder.getOrders().getUser().getEmail(),
                        "Image", saveOrder.getProduct().getImageURL(),
                        "ProductName", saveOrder.getProduct().getName(),
                        "Price", saveOrder.getProduct().getPrice().toString(),
                        "Qty", String.valueOf(saveOrder.getQuantity()),
                        "TotalAmount", String.valueOf(saveOrder.getOrders().getTotalAmount())
                );
                producer.sendMessage(objectMapper.writeValueAsString(map), RabbitMQConfig.ECOMMERCE_EXCHANGE, RabbitMQConfig.ROUTING_KEY_CANCEL_ORDER);

                return new ResponseModel(HttpStatus.OK, OrderDto.getOrder(saveOrder), SuccessMsg.ORDER_CANCEL);
            }
            return new ResponseModel(HttpStatus.NOT_ACCEPTABLE, null, SuccessMsg.ORDER_CANCEL_NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR+"\n"+e.getMessage());
        }
    }

    //Get All Orders --Admin Side
    @Override
    public ResponseModel GetAllOrders() {
        try {
            List<OrderDto> dtos = masterDao.getOrderItemsRepo().findAll()
                    .stream()
                    .map(OrderDto::getOrder)
                    .toList();
            return new ResponseModel(HttpStatus.OK, dtos, SuccessMsg.ORDER_FETCHED_SUCCESSFULLY);
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null,ErrorMsg.SERVER_ERROR+"\n"+e.getMessage());
        }
    }

    //Return Order --User Side
    @Override
    public ResponseModel returnOrder(Long orderId) {
        try {
            OrderItem orders = masterDao.getOrderItemsRepo().findById(orderId)
                    .orElseThrow(() -> new OrderNotFound(ErrorMsg.ORDER_NOT_FOUND));


            if (orders.getOrders().getOrderStatus() == OrderStatus.DELIVERED && orders.getUpdatedAt().plusDays(7).isAfter(LocalDateTime.now())) {
                if (orders.getOrderStatus() == OrderStatus.DELIVERED &&
                        orders.getUpdatedAt().plusDays(7).isAfter(LocalDateTime.now())) {
                    orders.setOrderStatus(OrderStatus.RETURN);
                    orders.setUpdatedAt(LocalDateTime.now());
                    OrderItem saveOrder = masterDao.getOrderItemsRepo().save(orders);

                    return new ResponseModel(HttpStatus.OK, OrderDto.getOrder(saveOrder), SuccessMsg.ORDER_RETURNED);

                }
            }
                return new ResponseModel(HttpStatus.BAD_REQUEST, null, SuccessMsg.ORDER_RETURN_NOT_ACCEPTABLE);


        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR+"\n"+e.getMessage());
        }
    }


}