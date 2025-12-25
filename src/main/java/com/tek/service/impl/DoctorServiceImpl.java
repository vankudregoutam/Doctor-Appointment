package com.tek.service.impl;

import com.tek.constants.DoctorConstants;
import com.tek.entity.*;
import com.tek.entity.dto.appointment.AppointmentResponseDTO;
import com.tek.entity.dto.doctor.DoctorLoginDTO;
import com.tek.entity.dto.doctor.DoctorRegistrationDTO;
import com.tek.entity.dto.doctor.DoctorResponseDTO;
import com.tek.entity.dto.leave.LeaveCreationDTO;
import com.tek.entity.dto.leave.LeaveResponseDTO;
import com.tek.entity.dto.user.UserResponseDTO;
import com.tek.exceptions.UnauthorizedUserException;
import com.tek.exceptions.doctor.DoctorInvalidCredentialsException;
import com.tek.exceptions.leave.InvalidLeaveDateException;
import com.tek.exceptions.leave.LeaveAlreadyExistsException;
import com.tek.exceptions.doctor.DoctorAlreadyExistsException;
import com.tek.exceptions.doctor.DoctorNotExistsException;
import com.tek.repository.AppointmentRepo;
import com.tek.repository.DoctorRepo;
import com.tek.repository.LeaveRepo;
import com.tek.service.DoctorService;
import com.tek.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;

    private final AppointmentRepo appointmentRepo;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final LeaveRepo leaveRepo;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    private static Doctor getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new UnauthorizedUserException("Unauthorized user. Login first.");

        Object principal = authentication.getPrincipal();

        if (principal instanceof Doctor)
            return (Doctor) principal;

        throw new UnauthorizedUserException("Invalid authentication principal type.");
    }

    @Override
    public void createDoctor(DoctorRegistrationDTO doctorRegistrationDTO) {
        Doctor doctor = doctorRepo.findByDoctorEmailAndPhoneNumber(doctorRegistrationDTO.getDoctorEmail(), doctorRegistrationDTO.getPhoneNumber());

        if (Objects.nonNull(doctor))
            throw new DoctorAlreadyExistsException("Doctor already exists with this email and phone number");

        Doctor doctor1 = mapper.map(doctorRegistrationDTO, Doctor.class);

        doctor1.setDoctorPassword(passwordEncoder.encode(doctorRegistrationDTO.getDoctorPassword()));

        doctor1.setCreatedEntity(doctor1.getDoctorName(), doctor1.getDoctorEmail());

        doctorRepo.save(doctor1);

    }

    @Override
    public String doctorLogin(DoctorLoginDTO doctorLoginDTO) {
        Doctor doctor = doctorRepo.findByDoctorEmail(doctorLoginDTO.getDoctorEmail());

        if (Objects.isNull(doctor))
            throw new DoctorNotExistsException("Doctor Does not exists");

        if (!passwordEncoder.matches(doctorLoginDTO.getDoctorPassword(), doctor.getDoctorPassword()))
            throw new DoctorInvalidCredentialsException("Invalid Credentials");

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(doctorLoginDTO.getDoctorEmail(), doctorLoginDTO.getDoctorPassword()));

            return jwtUtil.generateToken(doctorLoginDTO.getDoctorEmail());

        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createLeave(LeaveCreationDTO leaveCreationDTO) {

        Integer doctorId = getLoggedInUser().getDoctorId();

        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new DoctorNotExistsException("Doctor not found"));

        if (leaveCreationDTO.getToDate().isBefore(leaveCreationDTO.getFromDate())) {
            throw new InvalidLeaveDateException(DoctorConstants.MESSAGE_400_INVALID_LEAVE_DATE);
        }

        if (leaveRepo.existsByDoctor_DoctorIdAndDateOverlap(doctorId, leaveCreationDTO.getFromDate(), leaveCreationDTO.getToDate())) {
            throw new LeaveAlreadyExistsException(DoctorConstants.MESSAGE_409_LEAVE);
        }

        Leave leave = mapper.map(leaveCreationDTO, Leave.class);
        leave.setDoctor(doctor);

        leave.setCreatedEntity(doctor.getDoctorName(), doctor.getDoctorEmail());

        leaveRepo.save(leave);

    }

    @Override
    public List<AppointmentResponseDTO> fetchAllAppointments() {

        Integer doctorId = getLoggedInUser().getDoctorId();

        if (Objects.isNull(doctorId))
            throw new UnauthorizedUserException("Unauthorized access. Login first");

        // Manually map all fields to avoid ModelMapper configuration conflicts
        // This ensures consistent behavior regardless of type map configuration
        List<Appointment> appointmentList = appointmentRepo.findByDoctor_DoctorId(doctorId);

        return appointmentList.stream().map(appointment -> {

            // Manually map all fields to avoid ModelMapper configuration conflicts
            AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();
            
            // Map appointment fields
            appointmentResponseDTO.setProblem(appointment.getProblem());
            appointmentResponseDTO.setAppointmentDate(appointment.getAppointmentDate());
            appointmentResponseDTO.setAppointmentTime(appointment.getAppointmentTime());
            appointmentResponseDTO.setPatientName(appointment.getPatientName());
            appointmentResponseDTO.setPatientEmail(appointment.getPatientEmail());
            appointmentResponseDTO.setPhoneNumber(appointment.getPhoneNumber());
            appointmentResponseDTO.setPatientGender(appointment.getPatientGender());
            appointmentResponseDTO.setPatientAge(appointment.getPatientAge());

            // Manually set user details
            UserResponseDTO user = new UserResponseDTO();
            user.setUserId(appointment.getUser().getUserId());
            user.setUserName(appointment.getUser().getUserName());
            user.setUserEmail(appointment.getUser().getUserEmail());
            user.setPhoneNumber(appointment.getUser().getPhoneNumber());
            user.setGender(appointment.getUser().getGender());
            user.setUserAge(appointment.getUser().getUserAge());
            appointmentResponseDTO.setUser(user);

            // Manually set doctor details
            DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO();
            doctorResponseDTO.setDoctorId(appointment.getDoctor().getDoctorId());
            doctorResponseDTO.setDoctorName(appointment.getDoctor().getDoctorName());
            doctorResponseDTO.setDoctorEmail(appointment.getDoctor().getDoctorEmail());
            doctorResponseDTO.setSpecification(appointment.getDoctor().getSpecification());
            doctorResponseDTO.setPhoneNumber(appointment.getDoctor().getPhoneNumber());
            appointmentResponseDTO.setDoctor(doctorResponseDTO);

            return appointmentResponseDTO;

        }).toList();
    }

    @Override
    public List<LeaveResponseDTO> fetchLeaves() {
        Integer doctorId = getLoggedInUser().getDoctorId();

        if (Objects.isNull(doctorId))
            throw new UnauthorizedUserException("Unauthorized user. Login first.");

        List<Leave> leaves = leaveRepo.findByDoctor_DoctorId(doctorId);

        mapper.typeMap(Leave.class, LeaveResponseDTO.class)
                .addMappings(m -> m.skip(LeaveResponseDTO::setDoctor));

        return leaves.stream().map(leave -> mapper.map(leave, LeaveResponseDTO.class)).toList();
    }

}
