package com.nexusbusiness.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQPosConfig {

    public static final String TENANT_EXCHANGE = "nexus.tenant.exchange";
    public static final String POS_QUEUE = "pos.tenant.provisioning.queue";

    @Bean
    public FanoutExchange tenantExchange() {
        return new FanoutExchange(TENANT_EXCHANGE);
    }

    @Bean
    public Queue posTenantQueue() {
        return new Queue(POS_QUEUE, true); // true = Cola durable (sobrevive a reinicios de RabbitMQ)
    }

    @Bean
    public Binding binding(Queue posTenantQueue, FanoutExchange tenantExchange) {
        return BindingBuilder.bind(posTenantQueue).to(tenantExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}