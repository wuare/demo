#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * ${rootArtifactIdBig}ApiApplication
 */
@ComponentScan(basePackages = {"org.example"})
@EnableFeignClients
@SpringBootApplication
public class ${rootArtifactIdBig}ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(${rootArtifactIdBig}ApiApplication.class, args);
    }

}
