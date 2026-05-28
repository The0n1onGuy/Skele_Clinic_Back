package com.nexuscoreserver.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuración de RabbitMQ para el Core.
 * Migrado de FanoutExchange a TopicExchange para implementar enrutamiento modular y selectivo de inquilinos,
 * evitando que los módulos independientes (HIS/POS) reciban aprovisionamientos no correspondientes.
 */
@Configuration
public class RabbitMQCoreConfig {

    // Exchange tipo Topic para enrutamiento basado en patrones de claves (Routing Keys)
    public static final String TENANT_EXCHANGE = "nexus.tenant.exchange";
    public static final String ONBOARD_FAILED_QUEUE = "core.user.onboard.failed.queue";

    @Bean
    public TopicExchange tenantExchange() {
        return new TopicExchange(TENANT_EXCHANGE);
    }

    @Bean
    public Queue onboardFailedQueue() {
        return new Queue(ONBOARD_FAILED_QUEUE, true); // Durable = true
    }

    /**
     * Declara los bindings para recibir eventos de compensación SAGA.
     * Escuchará fallos de onboarding en los inquilinos ('user.onboard.failed') para revertir credenciales creadas.
     */
    @Bean
    public Declarables onboardFailedBindings(Queue onboardFailedQueue, TopicExchange tenantExchange) {
        Binding bindFailed = BindingBuilder.bind(onboardFailedQueue).to(tenantExchange).with("user.onboard.failed");
        return new Declarables(bindFailed);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Se instancía ObjectMapper directamente para evitar errores de inyección (NoSuchBeanDefinitionException) en tiempo de ejecución
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }
}