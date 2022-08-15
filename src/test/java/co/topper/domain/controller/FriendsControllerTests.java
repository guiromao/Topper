package co.topper.domain.controller;

import co.topper.AbstractionIntegrationTests;
import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FriendsControllerTests extends AbstractionIntegrationTests {

    static final String ROOT_PATH = "/topper/v1/friends";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    String token;
    Headers headers;

    String password = "pass";

    UserEntity user = new UserEntity(
            "user@mail.com",
            "username",
            new BCryptPasswordEncoder().encode(password),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyMap(),
            1000L, Instant.now(),
            Set.of(Role.USER)
    );

    UserEntity friend = new UserEntity(
            "friend@mail.com",
            "username-friend",
            new BCryptPasswordEncoder().encode("pass-friend"),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyMap(),
            1000L, Instant.now(),
            Set.of(Role.USER)
    );

    @BeforeEach
    void setup() throws Exception {
        userRepository.saveAll(Set.of(user, friend));

        token = getToken(user.getEmailId(), password);
        headers = new Headers(new Header("Authorization", "Bearer " + token));
    }

    @Test
    void testGetNotFriend() throws Exception {
        final String friendPath = ROOT_PATH + "?friendId=" + friend.getEmailId();

        String responseJson = get(friendPath, headers, withStatus(HttpStatus.BAD_REQUEST))
                .extract().asString();

        ErrorResponse errorResponse = mapper.readValue(responseJson, ErrorResponse.class);

        Assertions.assertNotNull(errorResponse);
    }

    @Test
    void testSendDuplicateRequest() throws Exception {
        friend = new UserEntity(
                "friend@mail.com",
                "username-friend",
                new BCryptPasswordEncoder().encode("pass-friend"),
                Collections.emptySet(),
                Set.of(user.getEmailId()),
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        userRepository.save(friend);

        String requestPath = ROOT_PATH + "/request?friendId=" + friend.getEmailId();

        String responseJson = post(requestPath, headers, null,
                withStatus(HttpStatus.BAD_REQUEST)).extract().asString();

        ErrorResponse errorResponse = mapper.readValue(responseJson, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
    }

    @Test
    void testSendRequestToFriend() throws Exception {
        user = new UserEntity(
                "user@mail.com",
                "username",
                new BCryptPasswordEncoder().encode(password),
                Set.of(friend.getEmailId()),
                Collections.emptySet(),
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        friend = new UserEntity(
                "friend@mail.com",
                "username-friend",
                new BCryptPasswordEncoder().encode("pass-friend"),
                Set.of(user.getEmailId()),
                Collections.emptySet(),
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        userRepository.saveAll(Set.of(user, friend));

        String requestPath = ROOT_PATH + "/request?friendId=" + friend.getEmailId();

        String responseJson = post(requestPath, headers, null,
                withStatus(HttpStatus.BAD_REQUEST)).extract().asString();

        ErrorResponse errorResponse = mapper.readValue(responseJson, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
    }

    @Test
    void testSendRequest() {
         final String requestPath = ROOT_PATH + "/request?friendId=" + friend.getEmailId();

         post(requestPath, headers, null, okStatus());

         assertEquals(Set.of(user.getEmailId()),
                 userRepository.findById(friend.getEmailId()).get().getRequestsReceivedIds());
    }

    @Test
    void testAcceptNonExistingRequest() throws Exception {
        String acceptPath = ROOT_PATH + "/request?friendId=" + friend.getEmailId();

        String responseJson = put(acceptPath, headers, null,
                withStatus(HttpStatus.BAD_REQUEST)).extract().asString();

        ErrorResponse errorResponse = mapper.readValue(responseJson, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
    }

    @Test
    void testAcceptRequest() {
        Set<String> requestsIds = Set.of(friend.getEmailId());
         user = new UserEntity(
                 "user@mail.com",
                 "username",
                 new BCryptPasswordEncoder().encode(password),
                 Collections.emptySet(),
                 requestsIds,
                 Collections.emptyMap(),
                 1000L, Instant.now(),
                 Set.of(Role.USER)
         );
         userRepository.save(user);

         String acceptPath = ROOT_PATH + "/request?friendId=" + friend.getEmailId();
         put(acceptPath, headers, null, okStatus());

         assertEquals(Set.of(user.getEmailId()),
                 userRepository.findById(friend.getEmailId()).get().getFriendsListIds());
         assertEquals(Set.of(friend.getEmailId()),
                 userRepository.findById(user.getEmailId()).get().getFriendsListIds());
    }

    @Test
    void testRefuseNonExistingRequest() throws Exception {
        String refusePath = ROOT_PATH + "/request?friendId=" + friend.getEmailId();

        String responseJson = delete(refusePath, headers,
                withStatus(HttpStatus.NOT_FOUND)).extract().asString();

        ErrorResponse errorResponse = mapper.readValue(responseJson, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
    }

    @Test
    void testRefuseRequest() {
        Set<String> requestsIds = Set.of(friend.getEmailId());
        user = new UserEntity(
                "user@mail.com",
                "username",
                new BCryptPasswordEncoder().encode(password),
                Collections.emptySet(),
                requestsIds,
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        userRepository.save(user);

        String acceptPath = ROOT_PATH + "/request?friendId=" + friend.getEmailId();
        delete(acceptPath, headers, okStatus());

        assertEquals(Collections.emptySet(),
                userRepository.findById(friend.getEmailId()).get().getRequestsReceivedIds());
        assertEquals(Collections.emptySet(),
                userRepository.findById(friend.getEmailId()).get().getFriendsListIds());
        assertEquals(Collections.emptySet(),
                userRepository.findById(user.getEmailId()).get().getFriendsListIds());
    }

    @Test
    void testDeleteNonExistingFriend() throws Exception {
        String deletePath = ROOT_PATH + "?friendId=" + friend.getEmailId();

        String responseJson = delete(deletePath, headers,
                withStatus(HttpStatus.BAD_REQUEST)).extract().asString();

        ErrorResponse errorResponse = mapper.readValue(responseJson, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
    }

    @Test
    void testDeleteFriend() {
        user = new UserEntity(
                "user@mail.com",
                "username",
                new BCryptPasswordEncoder().encode(password),
                Set.of(friend.getEmailId()),
                Collections.emptySet(),
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        friend = new UserEntity(
                "friend@mail.com",
                "username-friend",
                new BCryptPasswordEncoder().encode("pass-friend"),
                Set.of(user.getEmailId()),
                Collections.emptySet(),
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        userRepository.saveAll(Set.of(user, friend));

        String deletePath = ROOT_PATH + "?friendId=" + friend.getEmailId();

        delete(deletePath, headers, okStatus());

        assertEquals(Collections.emptySet(),
                userRepository.findById(user.getEmailId()).get().getFriendsListIds());
        assertEquals(Collections.emptySet(),
                userRepository.findById(friend.getEmailId()).get().getFriendsListIds());
    }

    @Test
    void testGetFriend() throws Exception {
        user = new UserEntity(
                "user@mail.com",
                "username",
                new BCryptPasswordEncoder().encode(password),
                Set.of(friend.getEmailId()),
                Collections.emptySet(),
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        friend = new UserEntity(
                "friend@mail.com",
                "username-friend",
                new BCryptPasswordEncoder().encode("pass-friend"),
                Set.of(user.getEmailId()),
                Collections.emptySet(),
                Collections.emptyMap(),
                1000L, Instant.now(),
                Set.of(Role.USER)
        );
        userRepository.saveAll(Set.of(user, friend));

        String friendPath = ROOT_PATH + "?friendId=" + friend.getEmailId();

        String responseJson = get(friendPath, headers, okStatus()).extract().asString();

        FriendDto test = mapper.readValue(responseJson, FriendDto.class);

        assertNotNull(test);
        assertEquals(friend.getEmailId(), test.getFriendId());
        assertEquals(friend.getUsername(), test.getUsername());
    }

}
