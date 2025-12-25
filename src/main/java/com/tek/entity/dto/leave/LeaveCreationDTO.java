package com.tek.entity.dto.leave;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveCreationDTO {

    @NotNull(message = "From date is required")
    @FutureOrPresent(message = "From date must be today or a future date")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    @FutureOrPresent(message = "To date must be today or a future date")
    private LocalDate toDate;

    @NotBlank(message = "Leave reason is required")
    @Size(min = 5, max = 255, message = "Leave reason must be between 5 and 255 characters")
    private String leaveReason;
}

