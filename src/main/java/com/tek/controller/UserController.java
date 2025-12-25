package com.tek.controller;

import com.tek.constants.AppointmentConstants;
import com.tek.constants.CommentConstants;
import com.tek.constants.DoctorConstants;
import com.tek.entity.dto.ResponseDTO;
import com.tek.entity.dto.appointment.AppointmentCreationDTO;
import com.tek.entity.dto.appointment.AppointmentResponseDTO;
import com.tek.entity.dto.comment.CommentCreationDTO;
import com.tek.entity.dto.comment.CommentResponseDTO;
import com.tek.entity.dto.doctor.DoctorResponseDTO;
import com.tek.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/fetchAppointments")
    public ResponseEntity<ResponseDTO> fetchAllAppointments() {
        List<AppointmentResponseDTO> appointments = userService.fetchAllAppointments();

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(AppointmentConstants.STATUS_200, AppointmentConstants.MESSAGE_200_FOUND, appointments));
    }

    @GetMapping("/displayDoctors")
    public ResponseEntity<ResponseDTO> displayDoctors() {
        List<DoctorResponseDTO> doctorResponseDTOS = userService.displayDoctors();

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(DoctorConstants.STATUS_200, DoctorConstants.MESSAGE_200_FOUND, doctorResponseDTOS));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ResponseDTO> searchDoctor(@PathVariable String keyword) {
        List<DoctorResponseDTO> doctor = userService.searchDoctor(keyword);

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(DoctorConstants.STATUS_200, DoctorConstants.MESSAGE_200_FOUND, doctor));
    }

    @PostMapping("/createAppointment")
    public ResponseEntity<ResponseDTO> createAppointment(@Valid @RequestBody AppointmentCreationDTO appointmentCreationDTO) {
        userService.createAppointment(appointmentCreationDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(AppointmentConstants.STATUS_201, AppointmentConstants.MESSAGE_201));
    }

    @PostMapping("/createComment")
    public ResponseEntity<ResponseDTO> createComment(@Valid @RequestBody CommentCreationDTO comment) {
        userService.createComment(comment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(CommentConstants.STATUS_201, CommentConstants.MESSAGE_201));
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<ResponseDTO> fetchAllComments() {
        List<CommentResponseDTO> commentResponseDTO = userService.fetchAllComments();

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(CommentConstants.STATUS_200, CommentConstants.MESSAGE_200_FOUND, commentResponseDTO));
    }

    @GetMapping("/fetchAll/{userId}")
    public ResponseEntity<ResponseDTO> fetchAllComment(@Valid @PathVariable Integer userId) {
        List<CommentResponseDTO> comments = userService.findByUserIdAdmin(userId);

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(CommentConstants.STATUS_200, CommentConstants.MESSAGE_200_FOUND, comments));
    }

}
