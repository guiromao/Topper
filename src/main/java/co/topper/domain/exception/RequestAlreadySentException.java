package co.topper.domain.exception;

public class RequestAlreadySentException extends RuntimeException {

    public RequestAlreadySentException(String userId) {
        super("Friend request already sent to user: " + userId);
    }

}
