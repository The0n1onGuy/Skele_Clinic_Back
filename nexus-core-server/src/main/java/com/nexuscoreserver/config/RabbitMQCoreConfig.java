package com.nexuscoreserver.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCoreConfig {

    // Usamos FanoutExchange para implementar el patrón Pub/Sub puro.
    // Copia el mensaje a todas las colas vinculadas.
    public static final String TENANT_EXCHANGE = "nexus.tenant.exchange";

    @Bean
    public FanoutExchange tenantExchange() {
        return new FanoutExchange(TENANT_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(); // Serialización estricta a JSON
    }
}