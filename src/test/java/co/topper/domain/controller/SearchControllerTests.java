package co.topper.domain.controller;

import co.topper.AbstractionIntegrationTests;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

class SearchControllerTests extends AbstractionIntegrationTests {

    static final String ROOT_PATH = "/topper/v1/search";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    String password = "pass";
    UserEntity user = new UserEntity(
            "user-123@mail.com",
            "username",
            new BCryptPasswordEncoder().encode(password),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyMap(),
            1000L, Instant.now(),
            Set.of(Role.USER)
    );

    String token;
    Headers headers;

    @BeforeEach
    void setup() throws Exception {
        teardown();
        userRepository.save(user);

        token = getToken(user.getEmailId(), password);
        headers = new Headers(new Header("Authorization", token));
    }

    @BeforeEach
    void teardown() {
        userRepository.deleteAll();
    }

    @Test
    void testSearch() throws Exception {
        final String searchPath = ROOT_PATH + "?text=all you need is love";

        String responseJson = get(searchPath, headers, responseOk()).extract().asString();

        List<TrackDto> test = Stream.of(mapper.readValue(responseJson, TrackDto[].class))
                .toList();

        Assertions.assertFalse(CollectionUtils.isEmpty(test));
    }

}
