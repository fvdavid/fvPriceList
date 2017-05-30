package fv.monster;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
@SpringBootApplication
public class FvPriceListApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(FvPriceListApplication.class).run(args);
    }
    
    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }
}
