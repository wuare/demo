#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.config.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * BaseJedisClusterConfig
 */

public abstract class BaseJedisClusterConfig {

    /**
     * 创建jedis客户端
     */
    protected JedisCluster getJedisCluster(String clusterNodes, String password, int timeout, int maxTotal) {

        String[] serverArray = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }

        // 线程池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);

        if (password != null && !password.isEmpty()) {
            return new JedisCluster(nodes, timeout, timeout, 3, password, jedisPoolConfig);
        } else {
            return new JedisCluster(nodes, timeout, 3, jedisPoolConfig);
        }

    }
}

