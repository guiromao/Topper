package co.topper.domain.exception;

public class TokenReadException extends RuntimeException {

    public TokenReadException(String resource) {
        super(String.format("Error reading %s from Token", resource));
    }

}
