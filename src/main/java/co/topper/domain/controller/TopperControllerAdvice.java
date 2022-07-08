package co.topper.domain.controller;

import co.topper.domain.exception.TailoredResponse;
import co.topper.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TopperControllerAdvice {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundExceptions(Exception ex) {
        TailoredResponse response = TailoredResponse.of(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
