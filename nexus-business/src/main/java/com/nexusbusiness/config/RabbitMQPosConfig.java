package com.nexusbusiness.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuración de mensajería RabbitMQ para el módulo POS (Business).
 * Configura la cola de Punto de Venta para recibir eventos de aprovisionamiento de inquilinos
 * dedicados a su línea de negocio, mitigando el desperdicio de base de datos.
 */
@Configuration
public class RabbitMQPosConfig {

    public static final String TENANT_EXCHANGE = "nexus.tenant.exchange";
    public static final String POS_QUEUE = "pos.tenant.provisioning.queue";

    @Bean
    public TopicExchange tenantExchange() {
        return new TopicExchange(TENANT_EXCHANGE);
    }

    @Bean
    public Queue posTenantQueue() {
        return new Queue(POS_QUEUE, true); // durable = true (cola durable)
    }

    /**
     * Declara los bindings de ruteo para el POS.
     * Escuchará eventos de aprovisionamiento específicos ('tenant.provision.pos') e híbridos ('tenant.provision.all').
     */
    @Bean
    public Declarables posTenantBindings(Queue posTenantQueue, TopicExchange tenantExchange) {
        Binding bindSpecific = BindingBuilder.bind(posTenantQueue).to(tenantExchange).with("tenant.provision.pos");
        Binding bindAll = BindingBuilder.bind(posTenantQueue).to(tenantExchange).with("tenant.provision.all");
        return new Declarables(bindSpecific, bindAll);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Se instancía ObjectMapper directamente para evitar errores de inyección en tiempo de ejecución
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }
}