package dev.mayur.ecommerce_backend.features.auth.repo;

import dev.mayur.ecommerce_backend.features.auth.entity.RefreshToken;
import dev.mayur.ecommerce_backend.features.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
