#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.rmq;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RmqSecondlyConfig
 */
@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.secondly.host")
public class RmqSecondlyConfig extends BaseRmqConfig {
    @Bean(name = "secondlyConnectionFactory")
    public ConnectionFactory firstConnectionFactory(
            @Value("${symbol_dollar}{spring.rabbitmq.secondly.host}") String host,
            @Value("${symbol_dollar}{spring.rabbitmq.secondly.port}") int port,
            @Value("${symbol_dollar}{spring.rabbitmq.secondly.username}") String username,
            @Value("${symbol_dollar}{spring.rabbitmq.secondly.password}") String password,
            @Value("${symbol_dollar}{spring.rabbitmq.secondly.virtual-host}") String virtualHost
    ) {
        return createConnectionFactory(host, port, username, password, virtualHost);
    }

    @Bean(name = "secondlyRabbitTemplate")
    public RabbitTemplate firstRabbitTemplate(
            @Qualifier("secondlyConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean(name = "secondlyFactory")
    public SimpleRabbitListenerContainerFactory firstFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Value("${symbol_dollar}{spring.rabbitmq.secondly.prefetchCount}") int prefetchCount,
            @Qualifier("secondlyConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return createSimpleRabbitListenerContainerFactory(configurer, prefetchCount, connectionFactory);
    }
}