package be.ordina.orientdb; /**
 * Created by ToVn on 20/05/15.
 */
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan("be.ordina.orientdb")
@EnableConfigurationProperties
public class SpringBootApplication {


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootApplication.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
