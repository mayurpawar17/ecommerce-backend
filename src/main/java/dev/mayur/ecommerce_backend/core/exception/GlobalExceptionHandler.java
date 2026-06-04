package dev.mayur.ecommerce_backend.core.exception;

import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
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

        return ResponseEntity.badRequest().body(apiResponse);
    }
}