package co.topper.util;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@PropertySource("classpath:credentials.properties")
public class TokenUtils {

    private static final int EXPIRY_DAYS = 10;

    public static String generateToken() {
        long expDate = Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli();

        String header = Base64.getUrlEncoder().encodeToString(("""
                {
                  "alg": "RS256",
                  "typ": "JWT"
                }""").getBytes(StandardCharsets.UTF_8));

        String payload = Base64.getUrlEncoder().encodeToString(("""
                {   "user_name": "test-id-123@mail.com"  }""").getBytes(StandardCharsets.UTF_8));

        return header + "." + payload;
    }

}
