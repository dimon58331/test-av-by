package by.av.test.testavby;

import by.av.test.testavby.config.JWTAuthenticationEntryPoint;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestAvByApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAvByApplication.class, args);
    }

    @Bean
    public JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JWTAuthenticationEntryPoint();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
