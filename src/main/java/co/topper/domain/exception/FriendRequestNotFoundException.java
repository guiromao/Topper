package co.topper.domain.exception;

public class FriendRequestNotFoundException extends RuntimeException {

    public FriendRequestNotFoundException(String userId) {
        super("Could not find friend request sent by user: " + userId);
    }

}
