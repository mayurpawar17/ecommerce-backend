package dev.mayur.ecommerce_backend.features.user.repo;

import dev.mayur.ecommerce_backend.features.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    Optional<Address> findByIdAndUserId(
            Long addressId,
            Long userId
    );


}


