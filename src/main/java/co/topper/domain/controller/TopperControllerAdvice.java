package co.topper.domain.controller;

import co.topper.domain.exception.ConnectivityFailureException;
import co.topper.domain.exception.EmptySearchTextException;
import co.topper.domain.exception.FriendRequestNotFoundException;
import co.topper.domain.exception.InvalidArgumentsException;
import co.topper.domain.exception.TailoredResponse;
import co.topper.domain.exception.ResourceNotFoundException;
import co.topper.domain.exception.UserAlreadyExistingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TopperControllerAdvice {

    @ExceptionHandler({ResourceNotFoundException.class, FriendRequestNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundExceptions(Exception ex) {
        TailoredResponse response = TailoredResponse.of(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConnectivityFailureException.class})
    public ResponseEntity<Object> handleRequestTimeOut(Exception ex) {
        TailoredResponse response = TailoredResponse.of(HttpStatus.REQUEST_TIMEOUT.value(),
                HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler({UserAlreadyExistingException.class, EmptySearchTextException.class,
            InvalidArgumentsException.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        TailoredResponse response = TailoredResponse.of(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
