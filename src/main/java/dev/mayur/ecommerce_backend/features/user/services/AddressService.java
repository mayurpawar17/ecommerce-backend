package dev.mayur.ecommerce_backend.features.user.services;

import dev.mayur.ecommerce_backend.features.user.dto.AddAddressRequestDTO;
import dev.mayur.ecommerce_backend.features.user.dto.AddressResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressResponseDTO addAddress(Long userId, AddAddressRequestDTO request);

    List<AddressResponseDTO> getAddresses(Long userId);

    AddressResponseDTO updateAddress(
            Long userId,
            Long addressId,
            AddAddressRequestDTO request);

    void deleteAddress(
            Long userId,
            Long addressId);
}


