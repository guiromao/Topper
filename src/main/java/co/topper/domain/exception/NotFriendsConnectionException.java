package co.topper.domain.exception;

public class NotFriendsConnectionException extends RuntimeException {

    public NotFriendsConnectionException(String id) {
        super(String.format("Exception: not friends with user ID %s", id));
    }

}
