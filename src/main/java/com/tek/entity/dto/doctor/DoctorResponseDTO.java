package com.tek.entity.dto.doctor;

import com.tek.entity.Appointment;
import com.tek.entity.Leave;
import lombok.Data;

import java.util.*;

@Data
public class DoctorResponseDTO {
    private Integer doctorId;
    private String doctorName;
    private String doctorEmail;
    private String specification;
    private String phoneNumber;
    private List<Appointment> appointments;
    private List<Leave> leaves;
}
