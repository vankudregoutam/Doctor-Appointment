package com.tek.entity.dto.appointment;

import com.tek.entity.Doctor;
import com.tek.entity.User;
import com.tek.entity.dto.doctor.DoctorResponseDTO;
import com.tek.entity.dto.user.UserResponseDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentResponseDTO {

    private String problem;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private UserResponseDTO user;
    private DoctorResponseDTO doctor;
    private String patientName;
    private String patientEmail;
    private String phoneNumber;
    private String patientGender;
    private Integer patientAge;

}
