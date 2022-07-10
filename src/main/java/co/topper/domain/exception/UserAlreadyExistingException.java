package co.topper.domain.exception;

public class UserAlreadyExistingException extends RuntimeException {

    public UserAlreadyExistingException(String field, String value) {
        super("User with " + field + " " + value +  " already exists in the database");
    }

}
