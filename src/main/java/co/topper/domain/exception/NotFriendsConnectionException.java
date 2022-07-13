package co.topper.domain.exception;

public class NotFriendsConnectionException extends RuntimeException {

    public NotFriendsConnectionException(String userId) {
        super("Exception for not yet being friends with user: " + userId);
    }

}
