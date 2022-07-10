package co.topper.domain.exception;

public class EmptySearchTextException extends RuntimeException {

    public EmptySearchTextException(Class clazz) {
        super("Cannot provide empty text when searching for: " + clazz.getSimpleName());
    }

}
