package com.cwc.certificate.driven.rabbitmq;




import org.springframework.amqp.core.*;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/26
 */

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "mail-exchange";
    public static final String QUEUE_NAME = "mail-queue";
    public static final String ROUTING_KEY = "mail.routingKey";

    @Bean
    public Exchange replyExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Queue replyQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding binding(Queue replyQueue, Exchange replyExchange) {
        return BindingBuilder.bind(replyQueue).to(replyExchange).with(ROUTING_KEY).noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReplyTimeout(5000);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
