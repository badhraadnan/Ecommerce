package com.ecom.orderService.utils;

import com.ecom.CommonEntity.dto.cartReminderDto;
import com.ecom.CommonEntity.entity.Cart;
import com.ecom.CommonEntity.entity.CartItem;
import com.ecom.CommonEntity.entity.User;
import com.ecom.commonRepository.dao.MasterDao;
import com.ecom.orderService.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Crones {

    @Autowired
    private MasterDao masterDao;

    @Autowired
    private RabbitMQProducer producer;

    @Autowired
    private ObjectMapper mapper;

    @Scheduled(cron = "0 0 */50 * * * ")
    public void cartReminder() {
        try {

            List<Cart> allCart = masterDao.getCartRepo().getAllCartByUserId();

            for (Cart cart : allCart) {

                List<CartItem> carts = masterDao.getCartItemsRepo().getAllCartByUser(cart.getUser().getUserId());


                    List<Map<String, String>> productList =carts.stream().map(items -> Map.of(

                            "Image", items.getProduct().getImageURL(),
                            "ProductName", items.getProduct().getName(),
                            "Qty", String.valueOf(items.getQuantity()),
                            "Price", String.valueOf(items.getPrice())

                    )).toList();


                Map<String,Object> map  = Map.of(
                        "UserName",cart.getUser().getFirstName(),
                        "Email",cart.getUser().getEmail(),
                        "Product",productList
                );
                producer.sendMessage(mapper.writeValueAsString(map), RabbitMQConfig.ECOMMERCE_EXCHANGE, RabbitMQConfig.ROUTING_KEY_CART);
                log.info("Reminder Send to RabbitMQ :");

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
