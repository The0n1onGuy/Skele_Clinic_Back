package com.nexushiscore.config;

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
 * Configuración de mensajería RabbitMQ para el módulo Clínico (HIS).
 * Configura la cola del HIS para suscribirse de forma selectiva a eventos de aprovisionamiento
 * dirigidos únicamente a este módulo clínico, resolviendo el problema de difusión indiscriminada.
 */
@Configuration
public class RabbitMQHisConfig {

    public static final String TENANT_EXCHANGE = "nexus.tenant.exchange";
    public static final String HIS_QUEUE = "his.tenant.provisioning.queue";
    public static final String HIS_ONBOARD_QUEUE = "his.user.onboard.queue";

    @Bean
    public TopicExchange tenantExchange() {
        return new TopicExchange(TENANT_EXCHANGE);
    }

    @Bean
    public Queue hisTenantQueue() {
        return new Queue(HIS_QUEUE, true); // durable = true (cola persiste reinicios del broker)
    }

    @Bean
    public Queue hisOnboardQueue() {
        return new Queue(HIS_ONBOARD_QUEUE, true); // Durable = true (cola persiste)
    }

    /**
     * Declara los bindings específicos de ruteo para el HIS.
     * La cola escuchará tanto mensajes dedicados ('tenant.provision.his') como híbridos ('tenant.provision.all').
     */
    @Bean
    public Declarables hisTenantBindings(Queue hisTenantQueue, TopicExchange tenantExchange) {
        Binding bindSpecific = BindingBuilder.bind(hisTenantQueue).to(tenantExchange).with("tenant.provision.his");
        Binding bindAll = BindingBuilder.bind(hisTenantQueue).to(tenantExchange).with("tenant.provision.all");
        return new Declarables(bindSpecific, bindAll);
    }

    /**
     * Declara los bindings para el alta de empleados (Onboarding) clínicos en el HIS.
     * Escucha eventos dedicados ('user.onboard.his') para persistir de manera asíncrona los expedientes locales.
     */
    @Bean
    public Declarables hisOnboardBindings(Queue hisOnboardQueue, TopicExchange tenantExchange) {
        Binding bindOnboard = BindingBuilder.bind(hisOnboardQueue).to(tenantExchange).with("user.onboard.his");
        return new Declarables(bindOnboard);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Se instancía ObjectMapper directamente para evitar errores de inyección en tiempo de ejecución
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }
}