package co.topper.domain.exception;

public class EmptyTrackNameException extends RuntimeException {

    public EmptyTrackNameException() {
        super("Cannot try to search tracks with empty text");
    }

}
