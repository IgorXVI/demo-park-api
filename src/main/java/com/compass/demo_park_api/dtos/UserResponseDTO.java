package com.compass.demo_park_api.dtos;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String role;
}
