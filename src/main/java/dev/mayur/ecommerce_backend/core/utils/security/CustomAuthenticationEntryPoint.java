package dev.mayur.ecommerce_backend.core.utils.security;

import tools.jackson.databind.ObjectMapper;
import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        ApiResponse<Object> apiResponse = ApiResponse.error("Please login to continue");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
