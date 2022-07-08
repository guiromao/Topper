package co.topper.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String id) {
        super("Could not load User with ID '" + id + "' from the Database");
    }

}
