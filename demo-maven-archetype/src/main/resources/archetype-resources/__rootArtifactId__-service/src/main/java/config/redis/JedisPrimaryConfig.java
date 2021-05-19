#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )

package ${package}.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.JedisCluster;

/**
 * JedisPrimaryConfig
 */
@Lazy
@Configuration
@ConditionalOnProperty(name = "spring.redis.primary.cluster.nodes")
public class JedisPrimaryConfig extends BaseJedisClusterConfig {


    @Value("${spring.redis.primary.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.primary.timeout}")
    private int timeout;

    @Value("${spring.redis.primary.maxTotal}")
    private int maxTotal;

    @Value("${spring.redis.primary.password}")
    private String password;

    @Bean(name = "primaryJedisCluster")
    @Primary
    public JedisCluster getJedisCluster() {

        return getJedisCluster(clusterNodes, password, timeout, maxTotal);

    }
}


