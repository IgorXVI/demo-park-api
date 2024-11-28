package com.compass.demo_park_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordDTO {
    @NotBlank
    @Size(min = 6, max = 6)
    private String password;

    @NotBlank
    @Size(min = 6, max = 6)
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 6)
    private String confirmNewPassword;
}
