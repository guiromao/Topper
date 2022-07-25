package co.topper.domain.exception;

public class NonExistingFriendRequestException extends RuntimeException {

    public NonExistingFriendRequestException(String friendId) {
        super(String.format("Did not receive friend request from user %s", friendId));
    }

}
