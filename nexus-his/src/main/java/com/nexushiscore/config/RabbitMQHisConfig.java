package com.nexushiscore.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQHisConfig {

    public static final String TENANT_EXCHANGE = "nexus.tenant.exchange";
    public static final String HIS_QUEUE = "his.tenant.provisioning.queue";

    @Bean
    public FanoutExchange tenantExchange() {
        return new FanoutExchange(TENANT_EXCHANGE);
    }

    @Bean
    public Queue hisTenantQueue() {
        return new Queue(HIS_QUEUE, true); // true = Cola durable (sobrevive a reinicios de RabbitMQ)
    }

    @Bean
    public Binding binding(Queue hisTenantQueue, FanoutExchange tenantExchange) {
        return BindingBuilder.bind(hisTenantQueue).to(tenantExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}