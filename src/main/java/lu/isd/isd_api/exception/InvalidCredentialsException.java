package lu.isd.isd_api.exception;

public class InvalidCredentialsException extends RuntimeException {
    // Default message
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }

    // Custom message support (recommended)
    public InvalidCredentialsException(String message) {
        super(message);
    }

}
