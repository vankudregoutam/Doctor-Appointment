package com.tek.service.impl;

import com.tek.entity.*;
import com.tek.entity.dto.appointment.AppointmentCreationDTO;
import com.tek.entity.dto.appointment.AppointmentResponseDTO;
import com.tek.entity.dto.comment.CommentCreationDTO;
import com.tek.entity.dto.comment.CommentResponseDTO;
import com.tek.entity.dto.doctor.DoctorResponseDTO;
import com.tek.entity.dto.user.UserLoginDTO;
import com.tek.entity.dto.user.UserRegistrationDTO;
import com.tek.entity.dto.user.UserResponseDTO;
import com.tek.exceptions.UnauthorizedUserException;
import com.tek.exceptions.appointment.AppointmentAlreadyExistsException;
import com.tek.exceptions.doctor.DoctorNotExistsException;
import com.tek.exceptions.user.InvalidCredentialsException;
import com.tek.exceptions.user.UserAlreadyExistsException;
import com.tek.exceptions.user.UserCanNotBeAdminException;
import com.tek.exceptions.user.UserNotExistsException;
import com.tek.repository.AppointmentRepo;
import com.tek.repository.CommentRepo;
import com.tek.repository.DoctorRepo;
import com.tek.repository.UserRepo;
import com.tek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo repo;

    private final DoctorRepo doctorRepo;

    private final AppointmentRepo appointmentRepo;

    private final CommentRepo commentRepo;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    private static User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new UnauthorizedUserException("Unauthorized user. Login first.");

        Object principal = authentication.getPrincipal();

        if (principal instanceof User)
            return (User) principal;

        throw new UnauthorizedUserException("Invalid authentication principal type.");
    }


    @Override
    public void createUser(UserRegistrationDTO userRegistrationDTO) {

        if (userRegistrationDTO.getRole().equals(Role.ADMIN))
            throw new UserCanNotBeAdminException("User can not be Admin");

        User user = repo.findByUserEmailAndPhoneNumber(userRegistrationDTO.getUserEmail(), userRegistrationDTO.getPhoneNumber());

        if (Objects.nonNull(user)) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user1 = mapper.map(userRegistrationDTO, User.class);

        user1.setUserPassword(passwordEncoder.encode(userRegistrationDTO.getUserPassword()));

        user1.setCreatedEntity(user1.getUserName(), user1.getUserEmail());
        repo.save(user1);
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {

        User user = repo.findByUserEmail(userLoginDTO.getUserEmail());

        if (Objects.isNull(user))
            throw new UserNotExistsException("User not exists: " + userLoginDTO.getUserEmail());

        if (!passwordEncoder.matches(userLoginDTO.getUserPassword(), user.getUserPassword()))
            throw new InvalidCredentialsException("Invalid Credentials");

        if (user.getRole().equals(Role.ADMIN)) {
            return "Admin Login Successful";
        }

        return "User Login Successful";

    }

    @Override
    public List<AppointmentResponseDTO> fetchAllAppointments() {

        Integer userId = getLoggedInUser().getUserId();

        // Manually map all fields to avoid ModelMapper configuration conflicts
        User user = repo.findById(userId).orElseThrow(() -> new UserNotExistsException("User does not exists."));

        List<Appointment> appointmentList;

        if (user.getRole().equals(Role.ADMIN))
            appointmentList = appointmentRepo.findAll();
        else
            appointmentList = appointmentRepo.findByUser_UserId(userId);

        return appointmentList.stream().map(appointment -> {
                    // Manually map all appointment fields
                    AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();
                    appointmentResponseDTO.setProblem(appointment.getProblem());
                    appointmentResponseDTO.setAppointmentDate(appointment.getAppointmentDate());
                    appointmentResponseDTO.setAppointmentTime(appointment.getAppointmentTime());
                    appointmentResponseDTO.setPatientName(appointment.getPatientName());
                    appointmentResponseDTO.setPatientEmail(appointment.getPatientEmail());
                    appointmentResponseDTO.setPhoneNumber(appointment.getPhoneNumber());
                    appointmentResponseDTO.setPatientGender(appointment.getPatientGender());
                    appointmentResponseDTO.setPatientAge(appointment.getPatientAge());

                    // Manually set doctor details
                    DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO();
                    doctorResponseDTO.setDoctorId(appointment.getDoctor().getDoctorId());
                    doctorResponseDTO.setDoctorName(appointment.getDoctor().getDoctorName());
                    doctorResponseDTO.setDoctorEmail(appointment.getDoctor().getDoctorEmail());
                    doctorResponseDTO.setSpecification(appointment.getDoctor().getSpecification());
                    doctorResponseDTO.setPhoneNumber(appointment.getDoctor().getPhoneNumber());
                    appointmentResponseDTO.setDoctor(doctorResponseDTO);

                    // Note: User details are not set here as this is for user/admin viewing their own appointments
                    // The user is the logged-in user, so we don't need to include it in the response

                    return appointmentResponseDTO;
                })
                .toList();
    }

    private DoctorResponseDTO mapDoctorToResponse(Doctor doctor) {
        DoctorResponseDTO dto = new DoctorResponseDTO();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setDoctorName(doctor.getDoctorName());
        dto.setDoctorEmail(doctor.getDoctorEmail());
        dto.setSpecification(doctor.getSpecification());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        return dto;
    }

    @Override
    public List<DoctorResponseDTO> searchDoctor(String keyword) {
        List<Doctor> doctors = doctorRepo.searchDoctors(keyword);

        return doctors.stream()
                .map(this::mapDoctorToResponse)
                .toList();
    }

    @Override
    public List<DoctorResponseDTO> displayDoctors() {

        List<Doctor> doctors = doctorRepo.findAll();

        return doctors.stream()
                .map(this::mapDoctorToResponse)
                .toList();
    }

    @Override
    public void createAppointment(AppointmentCreationDTO appointmentCreationDTO) {

        Integer userId = getLoggedInUser().getUserId();

        if (Objects.isNull(userId))
            throw new UnauthorizedUserException("Unauthorized user. Login first.");

        Appointment appointment = appointmentRepo.findByPhoneNumberAndProblem(appointmentCreationDTO.getPhoneNumber(), appointmentCreationDTO.getProblem());

        if (Objects.nonNull(appointment))
            throw new AppointmentAlreadyExistsException("Appointment already has been booked");

        User user = repo.findById(userId).orElseThrow(() -> new UserNotExistsException("User does not exists"));

        Doctor doctor = doctorRepo.findById(appointmentCreationDTO.getDoctorId()).orElseThrow(() -> new DoctorNotExistsException("Doctor does not exists"));

        Appointment appointment1 = new Appointment();
        BeanUtils.copyProperties(appointmentCreationDTO, appointment1, "user", "doctor", "userId", "doctorId");
        appointment1.setUser(user);
        appointment1.setDoctor(doctor);

        appointment1.setCreatedEntity(user.getUserName(), user.getUserEmail());

        appointmentRepo.save(appointment1);
    }

    @Override
    public void createComment(CommentCreationDTO comment) {

        Integer userId = getLoggedInUser().getUserId();

        if (Objects.isNull(userId))
            throw new UnauthorizedUserException("Unauthorized access. Login first.");

        User user = repo.findById(userId).orElseThrow(() -> new UserNotExistsException("User not found"));
        Comment comment1 = mapper.map(comment, Comment.class);
        comment1.setUser(user);
        comment1.setCreatedEntity(user.getUserName(), user.getUserEmail());

        commentRepo.save(comment1);
    }

    @Override
    public List<CommentResponseDTO> fetchAllComments() {
        Integer userId = getLoggedInUser().getUserId();

        // Create custom type map and skip nested mapping
        // Use emptyTypeMap to avoid implicit mapping conflicts
        org.modelmapper.TypeMap<Comment, CommentResponseDTO> commentTypeMap = 
                mapper.getTypeMap(Comment.class, CommentResponseDTO.class);
        
        if (commentTypeMap == null) {
            mapper.emptyTypeMap(Comment.class, CommentResponseDTO.class)
                    .implicitMappings()
                    .addMappings(m -> m.skip(CommentResponseDTO::setUser));
        }

        User loggedInUser = repo.findById(userId)
                .orElseThrow(() -> new UserNotExistsException("User does not exists."));

        List<Comment> commentsList;

        if (loggedInUser.getRole().equals(Role.ADMIN))
            commentsList = commentRepo.findAll();
        else
            commentsList = commentRepo.findByUser_UserId(userId);

        return commentsList.stream().map(comment -> {

                    CommentResponseDTO dto = mapper.map(comment, CommentResponseDTO.class);

                    // Build a lightweight user object just like Doctor in appointments
                    UserResponseDTO userResponse = new UserResponseDTO();
                    userResponse.setUserId(comment.getUser().getUserId());
                    userResponse.setUserName(comment.getUser().getUserName());
                    dto.setUser(userResponse);

                    return dto;
                })
                .toList();
    }

    @Override
    public List<CommentResponseDTO> findByUserIdAdmin(Integer userId) {

        Integer userId1 = getLoggedInUser().getUserId();
        Role role = getLoggedInUser().getRole();

        if (Objects.isNull(userId1))
            throw new UnauthorizedUserException("Unauthorized access. Login first");

        if (!role.equals(Role.ADMIN))
            throw new UnauthorizedUserException("You are not allowed to fetch data");

        repo.findById(userId).orElseThrow(()->new UserNotExistsException("User does not exists"));

        // Configure modelmapper to ignore user field
        // Use emptyTypeMap to avoid implicit mapping conflicts
        org.modelmapper.TypeMap<Comment, CommentResponseDTO> commentTypeMap = 
                mapper.getTypeMap(Comment.class, CommentResponseDTO.class);
        
        if (commentTypeMap == null) {
            mapper.emptyTypeMap(Comment.class, CommentResponseDTO.class)
                    .implicitMappings()
                    .addMappings(m -> m.skip(CommentResponseDTO::setUser));
        }

        List<Comment> comments = commentRepo.findByUser_UserId(userId);

        return comments.stream()
                .map(comment -> {
                    CommentResponseDTO dto = mapper.map(comment, CommentResponseDTO.class);
                    // Manually set user details
                    UserResponseDTO userResponse = new UserResponseDTO();
                    userResponse.setUserId(comment.getUser().getUserId());
                    userResponse.setUserName(comment.getUser().getUserName());
                    userResponse.setUserEmail(comment.getUser().getUserEmail());
                    userResponse.setPhoneNumber(comment.getUser().getPhoneNumber());
                    userResponse.setGender(comment.getUser().getGender());
                    userResponse.setUserAge(comment.getUser().getUserAge());
                    dto.setUser(userResponse);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDTO> fetchAllCommentsAdmin() {
        // Use emptyTypeMap to avoid implicit mapping conflicts
        org.modelmapper.TypeMap<Comment, CommentResponseDTO> commentTypeMap = 
                mapper.getTypeMap(Comment.class, CommentResponseDTO.class);
        
        if (commentTypeMap == null) {
            mapper.emptyTypeMap(Comment.class, CommentResponseDTO.class)
                    .implicitMappings()
                    .addMappings(m -> m.skip(CommentResponseDTO::setUser));
        }

        return commentRepo.findAll()
                .stream()
                .map(comment -> {
                    CommentResponseDTO dto = mapper.map(comment, CommentResponseDTO.class);
                    // Manually set user details
                    UserResponseDTO userResponse = new UserResponseDTO();
                    userResponse.setUserId(comment.getUser().getUserId());
                    userResponse.setUserName(comment.getUser().getUserName());
                    userResponse.setUserEmail(comment.getUser().getUserEmail());
                    userResponse.setPhoneNumber(comment.getUser().getPhoneNumber());
                    userResponse.setGender(comment.getUser().getGender());
                    userResponse.setUserAge(comment.getUser().getUserAge());
                    dto.setUser(userResponse);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
