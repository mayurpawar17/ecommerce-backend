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

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("Request URI: {}", request.getRequestURI());

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("Auth Header: {}", request.getHeader("Authorization"));

        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            log.info("Invalid JWT Token");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.extractSubject(token);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && user.isEnabled()) {
            CustomUserDetails userDetails = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
