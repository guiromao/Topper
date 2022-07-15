package co.topper.domain.exception;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String email) {
        super(String.format("User with email %s was not found", email));
    }

}
