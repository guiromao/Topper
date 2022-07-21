package co.topper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomConfig {

    @Bean
    public Random generateRandom() {
        return new Random();
    }

}
