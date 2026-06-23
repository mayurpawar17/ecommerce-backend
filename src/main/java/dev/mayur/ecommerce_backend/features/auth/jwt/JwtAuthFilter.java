package dev.mayur.ecommerce_backend.features.auth.jwt;

import dev.mayur.ecommerce_backend.features.auth.entity.User;
import dev.mayur.ecommerce_backend.features.auth.repo.UserRepository;
import dev.mayur.ecommerce_backend.features.auth.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Tells Spring to automatically detect this class and create a bean out of it (so it can be injected into your SecurityConfig)
@Component
@RequiredArgsConstructor
@Slf4j

/*
JwtAuthFilter : It is the security guard standing at the front door of your application.

Every single time a client (like a React frontend or mobile app) sends an HTTP request to a private API endpoint, this filter intercepts it before it reaches your @RestController. It looks for a token, verifies it, and tells Spring Security who is making the request.
 */

//OncePerRequestFilter: This is a specific Spring class guaranteeing that this filter executes exactly once for every single API request.
public class JwtAuthFilter extends OncePerRequestFilter {

    //handles parsing/verifying the token text
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;


    //This is the entry point. request contains incoming data (headers, URLs), response sends back data, and filterChain is the conveyor belt to pass the request to the next security guard.
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.debug("Request URI: {}", request.getRequestURI());

        String header = request.getHeader("Authorization");

        /*
        If there is no header, or if it doesn't start with "Bearer ",
        it means the user didn't provide a token (they are likely anonymous or trying to log in).
         */
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("Auth Header: {}", request.getHeader("Authorization"));

        //"Bearer " is exactly 7 characters long. This crops out that word and leaves only the raw encrypted JWT token string.
        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            log.error("Invalid JWT Token");
            filterChain.doFilter(request, response);
            return;
        }

        //Decrypts the token and reads the "Subject" field, which your application uses to store the user's email.
        String email = jwtUtil.extractSubject(token);

        //Checks your database to ensure this user actually still exists.
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && user.isEnabled()) {
            CustomUserDetails userDetails = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
