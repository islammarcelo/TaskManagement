package com.example.banquemisr.challenge05.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotNull(message = "username must not be null")
    @NotBlank(message = "username must not be empty")
    private String username;

    @NotNull(message = "email must not be null")
    @NotBlank(message = "email must not be empty")
    private String email;

    @NotNull(message = "password must not be null")
    @NotBlank(message = "password must not be empty")
    private String password;

    @NotNull(message = "role must not be null")
    @NotBlank(message = "role must not be empty")
    private String role; 
}
