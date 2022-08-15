package co.topper.domain.controller;

import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.exception.ConnectivityFailureException;
import co.topper.domain.exception.EmptySearchTextException;
import co.topper.domain.exception.ErrorResponse;
import co.topper.domain.exception.FriendRequestNotFoundException;
import co.topper.domain.exception.InvalidArgumentsException;
import co.topper.domain.exception.NonExistingFriendRequestException;
import co.topper.domain.exception.NotEnoughAvailableVotesException;
import co.topper.domain.exception.NotFriendsConnectionException;
import co.topper.domain.exception.RequestAlreadySentException;
import co.topper.domain.exception.ResourceNotFoundException;
import co.topper.domain.exception.TokenReadException;
import co.topper.domain.exception.UserAlreadyExistingException;
import co.topper.domain.exception.UserAlreadyFriendsException;
import co.topper.domain.exception.UserEmailNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@SpringBootTest
class TopperControllerAdviceTests {

    static List<Arguments> notFounds() {
        return List.of(
                Arguments.of(new ResourceNotFoundException("id", UserEntity.class)),
                Arguments.of(new FriendRequestNotFoundException("userId")),
                Arguments.of(new UserEmailNotFoundException("userEmail@mail.com"))
        );
    }

    static List<Arguments> timeouts() {
        return List.of(
                Arguments.of(new ConnectivityFailureException(Track.class))
        );
    }

    static List<Arguments> badRequests() {
        return List.of(
                Arguments.of(new UserAlreadyExistingException("field", "user-id")),
                Arguments.of(new EmptySearchTextException(Track.class)),
                Arguments.of(new InvalidArgumentsException()),
                Arguments.of(new TokenReadException("token")),
                Arguments.of(new NotEnoughAvailableVotesException(1000L, 500L)),
                Arguments.of(new NotFriendsConnectionException("user-id")),
                Arguments.of(new RequestAlreadySentException("user-id")),
                Arguments.of(new UserAlreadyFriendsException("user-id")),
                Arguments.of(new NonExistingFriendRequestException("friendId"))
        );
    }

    @Autowired
    TopperControllerAdvice controllerAdvice;

    @ParameterizedTest
    @MethodSource("notFounds")
    void testNotFoundExceptions(Exception exception) {
        ResponseEntity<Object> response = controllerAdvice.handleNotFoundExceptions(exception);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
    }

    @ParameterizedTest
    @MethodSource("timeouts")
    void testRequestTimeoutExceptions(Exception exception) {
        ResponseEntity<Object> response = controllerAdvice.handleRequestTimeOut(exception);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();

        Assertions.assertEquals(HttpStatus.REQUEST_TIMEOUT, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.REQUEST_TIMEOUT.value(), errorResponse.getCode());
    }

    @ParameterizedTest
    @MethodSource("badRequests")
    void testBadRequestsExceptions(Exception exception) {
        ResponseEntity<Object> response = controllerAdvice.handleBadRequest(exception);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
    }

}
