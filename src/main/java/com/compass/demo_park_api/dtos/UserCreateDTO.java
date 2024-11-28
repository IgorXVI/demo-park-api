package com.compass.demo_park_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateDTO {
    @NotBlank
    @Email
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;
}
