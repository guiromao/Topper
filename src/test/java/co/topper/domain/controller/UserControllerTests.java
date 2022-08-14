package co.topper.domain.controller;

import co.topper.AbstractionIntegrationTests;
import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

class UserControllerTests extends AbstractionIntegrationTests {

    static final String ROOT_PATH = "topper/v1/user";

    final UserEntity user = new UserEntity(
            "id-123@mail.com",
            "username-123",
            "pass", Collections.emptySet(),
            Collections.emptySet(), Collections.emptyMap(),
            1000L, Instant.now(), Set.of(Role.USER)
    );

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        teardown();
        userRepository.save(user);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
    }

    @Test
    void testGetUser() throws Exception {
        String emailId = user.getEmailId();
        String userPath = ROOT_PATH + "/" + emailId;

        String responseJson = get(userPath, null, responseOk()).extract().asString();

        UserDto test = objectMapper.readValue(responseJson, UserDto.class);

        Assertions.assertNotNull(test);
        Assertions.assertEquals(emailId, test.getEmailId());
    }

}
