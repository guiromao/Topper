package co.topper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BCryptConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptEncoder() {
        return new BCryptPasswordEncoder();
    }

}
