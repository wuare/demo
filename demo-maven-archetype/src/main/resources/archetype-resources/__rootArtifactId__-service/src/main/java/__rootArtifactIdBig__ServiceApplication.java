#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * ${rootArtifactIdBig}ServiceApplication
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.example"})
@EnableFeignClients
public class ${rootArtifactIdBig}ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(${rootArtifactIdBig}ServiceApplication.class, args);
    }

}
