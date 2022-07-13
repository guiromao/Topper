package co.topper.domain.exception;

public class UserAlreadyFriendsException extends RuntimeException {

    public UserAlreadyFriendsException(String userId) {
        super("You are already friends with user: " + userId);
    }

}
