package com.tek.service;

import com.tek.entity.dto.appointment.AppointmentResponseDTO;
import com.tek.entity.dto.doctor.DoctorLoginDTO;
import com.tek.entity.dto.doctor.DoctorRegistrationDTO;
import com.tek.entity.dto.leave.LeaveCreationDTO;
import com.tek.entity.dto.leave.LeaveResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface DoctorService {
    void createDoctor(DoctorRegistrationDTO doctorRegistrationDTO);

    void createLeave(LeaveCreationDTO leaveCreationDTO);

    String doctorLogin(@Valid DoctorLoginDTO doctorLoginDTO);

    List<AppointmentResponseDTO> fetchAllAppointments();

    List<LeaveResponseDTO> fetchLeaves();
}
