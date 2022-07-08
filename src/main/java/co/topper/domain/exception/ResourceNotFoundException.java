package co.topper.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String id, Class clazz) {
        super("Could not load " + clazz.getSimpleName() + " with ID '" + id + "' from the Database");
    }

}
