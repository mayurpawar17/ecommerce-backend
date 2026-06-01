package dev.mayur.ecommerce_backend.core.utils.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//@Builder
//@Getter
//@Setter
//@RequiredArgsConstructor
//public class ApiResponse <T>{
//    private boolean success;
//    private String message;
//    private T data;
//    private LocalDateTime timestamp;
//}



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"status", "message", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL) // Hides null fields (like pagination on single-object responses)
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Pagination pagination;


    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data, Pagination pagination) {
        return new ApiResponse<>("success", message, data, pagination);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null, null);
    }
}



