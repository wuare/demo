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
import org.springframework.context.annotation.Primary;

/**
 * RmqPrimaryConfig
 */
@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.primary.host")
public class RmqPrimaryConfig extends BaseRmqConfig {

    @Bean(name = "primaryConnectionFactory")
    @Primary
    public ConnectionFactory firstConnectionFactory(
            @Value("${symbol_dollar}{spring.rabbitmq.primary.host}") String host,
            @Value("${symbol_dollar}{spring.rabbitmq.primary.port}") int port,
            @Value("${symbol_dollar}{spring.rabbitmq.primary.username}") String username,
            @Value("${symbol_dollar}{spring.rabbitmq.primary.password}") String password,
            @Value("${symbol_dollar}{spring.rabbitmq.primary.virtual-host}") String virtualHost
    ) {
        return createConnectionFactory(host, port, username, password, virtualHost);
    }

    @Bean(name = "primaryRabbitTemplate")
    @Primary
    public RabbitTemplate firstRabbitTemplate(
            @Qualifier("primaryConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean(name = "primaryFactory")
    public SimpleRabbitListenerContainerFactory firstFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Value("${symbol_dollar}{spring.rabbitmq.primary.prefetchCount}") int prefetchCount,
            @Qualifier("primaryConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return createSimpleRabbitListenerContainerFactory(configurer, prefetchCount, connectionFactory);
    }


}
