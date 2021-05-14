#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.rmq;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;

/**
 * BaseRmqConfig
 *
 */
public abstract class BaseRmqConfig {
    protected CachingConnectionFactory createConnectionFactory(String host, int port, String username, String password, String virtualHost) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        return connectionFactory;
    }

    protected SimpleRabbitListenerContainerFactory createSimpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, int prefetchCount, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(prefetchCount);
        factory.setConcurrentConsumers(prefetchCount);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
