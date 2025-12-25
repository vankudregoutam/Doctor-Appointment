package com.tek.entity.dto.doctor;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DoctorLoginDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String doctorEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String doctorPassword;
}
