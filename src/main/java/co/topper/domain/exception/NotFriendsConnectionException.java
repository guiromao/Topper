package co.topper.domain.exception;

public class NotFriendsConnectionException extends RuntimeException {

    public NotFriendsConnectionException(String... userIds) {
        super("Exception: not friends: " + userIds);
    }

}
