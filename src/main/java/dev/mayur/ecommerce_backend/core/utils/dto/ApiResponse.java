package dev.mayur.ecommerce_backend.core.utils.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"status", "message", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL) // Hides null fields (like pagination on single-object responses)
public class ApiResponse<T> {
    private boolean status;
    private String message;
    private T data;
    private Pagination pagination;


    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data, Pagination pagination) {
        return new ApiResponse<>(true, message, data, pagination);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null);
    }
}



