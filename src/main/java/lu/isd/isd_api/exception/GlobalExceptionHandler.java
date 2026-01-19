package lu.isd.isd_api.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lu.isd.isd_api.dto.error.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle ResourceNotFoundException → 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        // Send empty errors map instead of null for consistency
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Collections.emptyMap() // empty map instead of null
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handle all other RuntimeExceptions → 500 Internal Server Error
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                Collections.emptyMap());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle validation exceptions with detailed field errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Extract all field errors
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle invalid credentials → 401 Unauthorized
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                Collections.emptyMap());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
