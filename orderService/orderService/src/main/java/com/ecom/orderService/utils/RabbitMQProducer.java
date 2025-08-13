package com.ecom.orderService.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message, String exchange, String routingKey) {
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                message
        );
        log.info("Message sent to RabbitMQ: {}", message);
}
}
