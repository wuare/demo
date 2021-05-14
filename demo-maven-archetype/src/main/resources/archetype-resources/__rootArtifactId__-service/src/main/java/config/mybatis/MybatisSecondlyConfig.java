#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据源配置
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ConditionalOnProperty(name = "spring.datasource.secondly.url")
@MapperScan(basePackages = "${package}.mapper.secondly", sqlSessionTemplateRef = "secondlySqlSessionTemplate")
public class MybatisSecondlyConfig extends BaseMybatisConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondly")
    DataSource secondlyDataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean
    SqlSessionFactory secondlySqlSessionFactory(DataSource secondlyDataSource) throws Exception {

        return sqlSessionFactory(secondlyDataSource, new String[]{"classpath:mapper/secondly/*.xml"});
    }

    @Bean
    SqlSessionTemplate secondlySqlSessionTemplate(SqlSessionFactory secondlySqlSessionFactory) {

        return sqlSessionTemplate(secondlySqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager secondlyPlatformTransactionManager() {
        return new DataSourceTransactionManager(secondlyDataSource());
    }

}
