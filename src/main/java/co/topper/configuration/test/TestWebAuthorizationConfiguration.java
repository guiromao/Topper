package co.topper.configuration.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

@Profile("test")
@Configuration
@Order(1)
public class TestWebAuthorizationConfiguration extends AuthorizationServerConfigurerAdapter {

}
