package lu.isd.isd_api.exception;

public class ResourceNotFoundException extends RuntimeException {

    // Existing constructor (KEEP IT)
    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s not found with id: %d", resource, id));
    }

    // ADD THIS constructor (for flexible usage)
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format(
                "%s not found with %s: %s",
                resource,
                field,
                value));
    }

    public ResourceNotFoundException(String resource, String key) {
        super(String.format("%s not found with key: %s", resource, key));
    }
}
