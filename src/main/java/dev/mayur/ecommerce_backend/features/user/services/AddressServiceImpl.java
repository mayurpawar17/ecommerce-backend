package dev.mayur.ecommerce_backend.features.user.services;

import dev.mayur.ecommerce_backend.core.exception.custom.ResourceNotFoundException;
import dev.mayur.ecommerce_backend.features.auth.entity.User;
import dev.mayur.ecommerce_backend.features.auth.repo.UserRepository;
import dev.mayur.ecommerce_backend.features.user.dto.AddAddressRequestDTO;
import dev.mayur.ecommerce_backend.features.user.dto.AddressResponseDTO;
import dev.mayur.ecommerce_backend.features.user.entity.Address;
import dev.mayur.ecommerce_backend.features.user.repo.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public AddressResponseDTO addAddress(Long userId, AddAddressRequestDTO request) {

        // 1. Fetch the user profile
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Handle default address resetting logic
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            List<Address> addresses = addressRepository.findByUserId(userId);
            addresses.forEach(address -> address.setIsDefault(false));
            addressRepository.saveAll(addresses);
        }

        // 3. TRADITIONAL WAY: Instantiate via standard constructor and setters
        Address address = new Address();
        address.setUser(user);
        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber()); // Handled perfectly!
        address.setLine1(request.getLine1());
        address.setLine2(request.getLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setIsDefault(request.getIsDefault());

        // 4. Save to database
        Address savedAddress = addressRepository.save(address);

        // 5. Convert entity back to response payload
        return mapToResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDTO> getAddresses(Long userId) {

        return addressRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }


    @Override
    @Transactional
    public AddressResponseDTO updateAddress(Long userId, Long addressId, AddAddressRequestDTO request) {

        Address address = addressRepository.findByIdAndUserId(addressId, userId).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (Boolean.TRUE.equals(request.getIsDefault())) {

            List<Address> addresses = addressRepository.findByUserId(userId);

            addresses.forEach(a -> a.setIsDefault(false));

            addressRepository.saveAll(addresses);
        }

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setLine1(request.getLine1());
        address.setLine2(request.getLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setCountry(request.getCountry());
        address.setIsDefault(request.getIsDefault());

        Address updated = addressRepository.save(address);

        return mapToResponse(updated);
    }


    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {

        Address address = addressRepository.findByIdAndUserId(addressId, userId).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        addressRepository.delete(address);
    }


    private AddressResponseDTO mapToResponse(Address address) {
        // 1. Instantiate the DTO using the default constructor
        AddressResponseDTO response = new AddressResponseDTO();

        // 2. Map fields manually using setters
        response.setId(address.getId());
        response.setFullName(address.getFullName());
        response.setPhoneNumber(address.getPhoneNumber()); // Fully mapped now!
        response.setLine1(address.getLine1());
        response.setLine2(address.getLine2());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());
        response.setCountry(address.getCountry());
        response.setIsDefault(address.getIsDefault());

        // 3. Return the populated DTO
        return response;
    }
}
