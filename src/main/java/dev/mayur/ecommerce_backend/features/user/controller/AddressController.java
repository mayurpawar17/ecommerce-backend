package dev.mayur.ecommerce_backend.features.user.controller;

import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import dev.mayur.ecommerce_backend.features.auth.service.CustomUserDetails;
import dev.mayur.ecommerce_backend.features.user.dto.AddAddressRequestDTO;
import dev.mayur.ecommerce_backend.features.user.dto.AddressResponseDTO;
import dev.mayur.ecommerce_backend.features.user.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users/me/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponseDTO>> addAddress(@AuthenticationPrincipal CustomUserDetails userDetails,

                                                                      @Valid @RequestBody AddAddressRequestDTO request) {
        AddressResponseDTO createdAddress = addressService.addAddress(userDetails.getId(), request);

        ApiResponse<AddressResponseDTO> body = ApiResponse.success("Address created successfully!", createdAddress);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getAddresses(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<AddressResponseDTO> addressResponseDTO = addressService.getAddresses(userDetails.getId());
        ApiResponse<List<AddressResponseDTO>> body = ApiResponse.success("Address successfully fetched!", addressResponseDTO);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }


    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> updateAddress(

            @PathVariable Long addressId,

            @AuthenticationPrincipal CustomUserDetails currentUser,

            @Valid @RequestBody AddAddressRequestDTO request) {
        AddressResponseDTO updatedAddress = addressService.updateAddress(currentUser.getId(), addressId, request);
        ApiResponse<AddressResponseDTO> body = ApiResponse.success("Address updated successfully!", updatedAddress);


        return ResponseEntity.status(HttpStatus.OK).body(body);
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(

            @PathVariable Long addressId,

            @AuthenticationPrincipal CustomUserDetails currentUser) {

        addressService.deleteAddress(currentUser.getId(), addressId);


        ApiResponse<String> body = ApiResponse.success("Address updated successfully!", null);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
