package co.topper.domain.controller.service;

import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Service used to create access Tokens for the integration tests
 */
@Component
@PropertySource("classpath:credentials.properties")
public class TokenService {

    @Value("${app.secret}")
    private String secret;

    private final UserRepository userRepository;
    //private final MockMvc mockMvc;

    @Autowired
    public TokenService(UserRepository userRepository
                        /*MockMvc mockMvc*/) {
        this.userRepository = userRepository;
        //this.mockMvc = mockMvc;
    }

    public void createUser() {
        UserEntity user = UserEntity.create("user-test@mail.com", "test", "test-123");
        userRepository.save(user);
    }

    public void deleteUser() {
        userRepository.deleteById("test@mail.com");
    }

    private static final Integer TOKEN_VALIDITY = 5 * 60 * 60;

    /*public String generateToken() {
        Map<String, String> headers = new HashMap<>();
        headers.put("username", "test@mail.com");
        headers.put("password", "test-123");
        headers.put("grant_type", "password");
        headers.put("scopes", "read write");
        String body = "{\"username\":\"test@mail.com\", \"password\":\"test-123\"}";

        MvcResult result = null;
        try {
            result = mockMvc.perform(MockMvcRequestBuilders.post("/v2/token")
                            .content(body));
                   // .andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String response = null;
        try {
            response = result.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response = response.replace("{\"access_token\": \"", "");
        String token = response.replace("\"}", "");

        return token;
    }*/

}
