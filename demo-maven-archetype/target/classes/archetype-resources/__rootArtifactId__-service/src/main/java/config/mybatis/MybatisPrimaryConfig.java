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
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据源配置
 */

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ConditionalOnProperty(name = "spring.datasource.primary.url")
@MapperScan(basePackages = "${package}.mapper.primary", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class MybatisPrimaryConfig extends BaseMybatisConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    DataSource primaryDataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean
    SqlSessionFactory primarySqlSessionFactory(DataSource primaryDataSource) throws Exception {

        return sqlSessionFactory(primaryDataSource, new String[]{"classpath:mapper/primary/*.xml"});
    }

    @Bean
    SqlSessionTemplate primarySqlSessionTemplate(SqlSessionFactory primarySqlSessionFactory) {

        return sqlSessionTemplate(primarySqlSessionFactory);
    }

    @Bean
    @Primary
    public PlatformTransactionManager primaryPlatformTransactionManager() {
        return new DataSourceTransactionManager(primaryDataSource());
    }
}
