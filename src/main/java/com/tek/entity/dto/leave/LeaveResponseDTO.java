package com.tek.entity.dto.leave;

import com.tek.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveResponseDTO {
    private Integer leaveId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String leaveReason;
    private Doctor doctor;
}
