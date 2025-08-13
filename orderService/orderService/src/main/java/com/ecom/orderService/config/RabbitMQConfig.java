package com.ecom.orderService.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //Place Order
    public static final String ECOMMERCE_EXCHANGE = "ecommerce.exchange";
    public static final String ROUTING_KEY_ORDER_PROCESSOR = "ecommerce.order";
    public static final String QUEUE_PLACE_ORDER = "ecommerce.place.order";

    //Cart Reminder
    public static final String ROUTING_KEY_CART = "ecommerce.cart";
    public static final String QUEUE_CART_REMINDER = "ecommerce.cart.reminder";

    //Cancel Order
    public static final String ROUTING_KEY_CANCEL_ORDER = "cancel.order";
    public static final String QUEUE_CANCEL_ORDER = "ecommerce.cancel.order";


    @Bean(name = "EcommerceExchange")
    public DirectExchange EcommerceExchange() {
        return new DirectExchange(ECOMMERCE_EXCHANGE);
    }

    @Bean(name = "PlaceOrderQueue")
    public Queue PlaceOrderQueue() {
        return new Queue(QUEUE_PLACE_ORDER, false);
    }

    @Bean
    public Binding placeOrderBinding(
            @Qualifier("PlaceOrderQueue") Queue queue,
            @Qualifier("EcommerceExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_ORDER_PROCESSOR);
    }


    @Bean(name = "CartReminderQueue")
    public Queue CartReminderQueue() {
        return new Queue(QUEUE_CART_REMINDER, false);
    }

    @Bean
    public Binding cartReminderBinding(
            @Qualifier("CartReminderQueue") Queue queue,
            @Qualifier("EcommerceExchange") DirectExchange exchange
    ) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_CART);
    }


    @Bean(name = "orderCancel")
    public Queue orderCancel() {
        return new Queue(QUEUE_CANCEL_ORDER, false);
    }

    @Bean
    public Binding cancelOrder(
            @Qualifier("EcommerceExchange") DirectExchange exchange,
            @Qualifier("orderCancel") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_CANCEL_ORDER);
    }

}
