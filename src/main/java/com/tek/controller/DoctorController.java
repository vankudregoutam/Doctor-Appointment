package com.tek.controller;

import com.tek.constants.AppointmentConstants;
import com.tek.constants.DoctorConstants;
import com.tek.entity.dto.ResponseDTO;
import com.tek.entity.dto.appointment.AppointmentResponseDTO;
import com.tek.entity.dto.doctor.DoctorLoginDTO;
import com.tek.entity.dto.doctor.DoctorRegistrationDTO;
import com.tek.entity.dto.leave.LeaveCreationDTO;
import com.tek.entity.dto.leave.LeaveResponseDTO;
import com.tek.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService service;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createDoctor(@Valid  @RequestBody DoctorRegistrationDTO doctorRegistrationDTO) {
        service.createDoctor(doctorRegistrationDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(DoctorConstants.STATUS_201, DoctorConstants.MESSAGE_201));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginDoctor(@Valid @RequestBody DoctorLoginDTO doctorLoginDTO) {
        String msg = service.doctorLogin(doctorLoginDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(DoctorConstants.STATUS_200, msg));
    }

    @GetMapping("/fetchAllAppointment")
    public ResponseEntity<ResponseDTO> fetchAllAppointment() {
        List<AppointmentResponseDTO> appointments = service.fetchAllAppointments();

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(AppointmentConstants.STATUS_200, AppointmentConstants.MESSAGE_200_FOUND, appointments));
    }

    @PostMapping("/createLeave")
    public ResponseEntity<ResponseDTO> createLeave(@Valid @RequestBody LeaveCreationDTO leaveCreationDTO) {
        service.createLeave(leaveCreationDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(DoctorConstants.STATUS_201, "Leave Created Successfully"));
    }

    @GetMapping("/fetchLeaves")
    public ResponseEntity<ResponseDTO> fetchLeaves() {
        List<LeaveResponseDTO> leaveResponseDTOS = service.fetchLeaves();

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(DoctorConstants.STATUS_200, "Leaves found successfully", leaveResponseDTOS));
    }

}
