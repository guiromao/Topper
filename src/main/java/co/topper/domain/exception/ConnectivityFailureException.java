package co.topper.domain.exception;

public class ConnectivityFailureException extends RuntimeException {

    public ConnectivityFailureException(Class clazz) {
        super("Connectivity failure while trying to search for: " + clazz.getSimpleName());
    }
}
