package co.topper.domain.service;

import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.TokenReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class TokenReader {
    private final ObjectMapper objectMapper;

    public TokenReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getUserEmail(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(payload, new TypeReference<>() {});

            return jsonMap.get("user_name").toString();
        } catch (Exception ex) {
            throw new TokenReadException("user_name");
        }

    }


}
