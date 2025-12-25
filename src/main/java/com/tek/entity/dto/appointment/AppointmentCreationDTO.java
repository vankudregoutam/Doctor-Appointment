package com.tek.entity.dto.appointment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDTO {

    @NotBlank(message = "Problem description is required")
    @Size(min = 5, max = 255, message = "Problem must be between 5 and 255 characters")
    private String problem;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date must be today or later")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    @NotBlank(message = "Patient name is required")
    @Size(min = 3, max = 50, message = "Patient name should be between 3 and 50 characters")
    private String patientName;

    @NotNull(message = "Patient age is required")
    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 120, message = "Age cannot exceed 120")
    private Integer patientAge;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian number")
    private String phoneNumber;

    @NotBlank(message = "Patient email is required")
    @Email(message = "Invalid email format")
    private String patientEmail;

    @NotBlank(message = "Patient gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$",
            message = "Gender must be Male, Female, or Other")
    private String patientGender;

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

}
