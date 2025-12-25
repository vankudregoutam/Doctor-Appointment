package com.tek.entity.dto.doctor;

import com.tek.entity.Appointment;
import com.tek.entity.Leave;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class DoctorRegistrationDTO {

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

    @NotBlank(message = "Doctor name is required")
    @Size(min = 3, max = 50, message = "Doctor name must be between 3 and 50 characters")
    private String doctorName;

    @NotBlank(message = "Specification is required")
    @Size(min = 3, max = 50, message = "Specification must be between 3 and 50 characters")
    private String specification;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian number starting with 6-9"
    )
    private String phoneNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointment;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Leave> leave;
}
