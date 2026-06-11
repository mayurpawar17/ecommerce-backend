package dev.mayur.ecommerce_backend.core.exception;

import dev.mayur.ecommerce_backend.core.exception.custom.FailedToDeleteException;
import dev.mayur.ecommerce_backend.core.exception.custom.InvalidFileException;
import dev.mayur.ecommerce_backend.core.exception.custom.UserNotFoundException;
import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        // TRADITIONAL WAY: Instantiate via standard constructor and setters
        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("error");
        apiResponse.setMessage("Validation failed");
        apiResponse.setData(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }


    // User not found maps to 404 Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    // Invalid file maps to 404 Not Found
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidException(InvalidFileException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Failed to delete maps to 404 Not Found
    @ExceptionHandler(FailedToDeleteException.class)
    public ResponseEntity<ApiResponse<Object>> handleFailedToDeleteException(FailedToDeleteException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}