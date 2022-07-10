package co.topper.domain.exception;

public class InvalidArgumentsException extends IllegalArgumentException {

    public InvalidArgumentsException(Object... objects) {
        super("Values provided " + objects + " are invalid for the request made");
    }
}
