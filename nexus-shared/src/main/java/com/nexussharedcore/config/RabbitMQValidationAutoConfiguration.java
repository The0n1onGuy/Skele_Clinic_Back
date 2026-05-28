package com.nexussharedcore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.rabbitmq.client.Channel;

@Configuration
@ConditionalOnClass(RabbitTemplate.class)
public class RabbitMQValidationAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQValidationAutoConfiguration.class);

    @Bean
    public ApplicationRunner rabbitMQStartupValidator(RabbitTemplate rabbitTemplate) {
        return args -> {
            try {
                rabbitTemplate.execute(Channel::isOpen);
                log.info("Conexión con RabbitMQ establecida exitosamente.");
            } catch (AmqpConnectException e) {
                log.error("=========================================================");
                log.error("ALERTA CRÍTICA: El servicio de mensajería no se encuentra activo o tiene un problema.");
                log.error("Operaciones dependientes de colas fallarán temporalmente. El servicio principal sigue activo.");
                log.error("=========================================================");
            }
        };
    }
}
