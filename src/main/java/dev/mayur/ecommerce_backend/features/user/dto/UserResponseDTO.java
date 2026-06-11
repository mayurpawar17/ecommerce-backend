package dev.mayur.ecommerce_backend.features.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String avatarUrl;
}
