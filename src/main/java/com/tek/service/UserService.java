package com.tek.service;

import com.tek.entity.dto.appointment.AppointmentCreationDTO;
import com.tek.entity.dto.appointment.AppointmentResponseDTO;
import com.tek.entity.dto.comment.CommentCreationDTO;
import com.tek.entity.dto.comment.CommentResponseDTO;
import com.tek.entity.dto.doctor.DoctorResponseDTO;
import com.tek.entity.dto.user.UserLoginDTO;
import com.tek.entity.dto.user.UserRegistrationDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    void createUser(@Valid UserRegistrationDTO userRegistrationDTO);

    String login(@Valid UserLoginDTO userLoginDTO);

    List<AppointmentResponseDTO> fetchAllAppointments();

    List<DoctorResponseDTO> searchDoctor(String keyword);

    List<DoctorResponseDTO> displayDoctors();

    void createAppointment(AppointmentCreationDTO appointmentCreationDTO);

    void createComment(@Valid CommentCreationDTO comment);

    List<CommentResponseDTO> fetchAllComments();

    List<CommentResponseDTO> findByUserIdAdmin(@Valid Integer userId);

    List<CommentResponseDTO> fetchAllCommentsAdmin();
}
