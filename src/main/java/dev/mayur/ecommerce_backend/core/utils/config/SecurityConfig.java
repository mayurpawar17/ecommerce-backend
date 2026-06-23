package dev.mayur.ecommerce_backend.core.utils.config;

import dev.mayur.ecommerce_backend.core.utils.security.CustomAccessDeniedHandler;
import dev.mayur.ecommerce_backend.core.utils.security.CustomAuthenticationEntryPoint;
import dev.mayur.ecommerce_backend.features.auth.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity// Allows you to secure individual Controller methods using @PreAuthorize
@RequiredArgsConstructor// Automatically builds a constructor to inject the final fields below
public class SecurityConfig {

    //custom security guard. It intercepts incoming requests,reads the JWT token from the header, and checks if it's valid.
    private final JwtAuthFilter jwtAuthFilter;
    //This handles 401 Unauthorized errors (e.g., when a user tries to access a private page but didn't log in or provided a fake token).
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    //handles 403 Forbidden errors (e.g., a regular logged-in user tries to access an admin-only page).
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //Disable CSRF because JWT-based APIs do not use browser sessions/cookies
        http.csrf(AbstractHttpConfigurer::disable);
        //Make the application stateless. No server-side sessions will store user state.
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //Define the URL access permissions (The Gatekeeper Rules)
        //Allow anyone to access authentication routes (login, register, forgot password)
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/api/v1/auth/**").permitAll());
        // Only allow users with the 'ADMIN' role to touch admin routes
        // ALL other requests not specified above require a successful login
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/api/v1/admin/**").hasRole("ADMIN").anyRequest().authenticated());
        //Tell Spring how to respond when security checks fail
        //Triggers when a user tries to access a secured route without logging in (401)
        //Triggers when a logged-in user tries to access a route they don't have permission for (403)
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler));

        //Inject your custom JWT filter into Spring's security engine.
        //This ensures we extract and validate the JWT token BEFORE the traditional username/password filter runs.
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        //Build and return this customized security configuration
        return http.build();
    }
}
