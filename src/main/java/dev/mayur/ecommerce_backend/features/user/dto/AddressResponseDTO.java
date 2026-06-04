package dev.mayur.ecommerce_backend.features.user.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class AddressResponseDTO {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String line1;

    private String line2;

    private String city;

    private String state;

    private String pincode;

    private String country;

    private Boolean isDefault;
}
