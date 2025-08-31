package com.order.processing.service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange("inventory.exchange");
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange("payment.exchange");
    }

    // Queues
    @Bean
    public Queue inventoryCheckQueue() {
        return new Queue("inventory.check.queue");
    }

    @Bean
    public Queue inventoryReleaseQueue() {
        return new Queue("inventory.release.queue");
    }

    @Bean
    public Queue paymentProcessQueue() {
        return new Queue("payment.process.queue");
    }

    @Bean
    public Queue paymentRefundQueue() {
        return new Queue("payment.refund.queue");
    }

    // Bindings
    @Bean
    public Binding inventoryCheckBinding(Queue inventoryCheckQueue, DirectExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryCheckQueue).to(inventoryExchange).with("inventory.check");
    }

    @Bean
    public Binding inventoryReleaseBinding(Queue inventoryReleaseQueue, DirectExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryReleaseQueue).to(inventoryExchange).with("inventory.release");
    }

    @Bean
    public Binding paymentProcessBinding(Queue paymentProcessQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentProcessQueue).to(paymentExchange).with("payment.process");
    }

    @Bean
    public Binding paymentRefundBinding(Queue paymentRefundQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentRefundQueue).to(paymentExchange).with("payment.refund");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
