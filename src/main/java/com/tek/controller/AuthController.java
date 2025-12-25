package com.tek.controller;

import com.tek.constants.DoctorConstants;
import com.tek.constants.UserConstants;
import com.tek.entity.User;
import com.tek.entity.dto.ResponseDTO;
import com.tek.entity.dto.doctor.DoctorLoginDTO;
import com.tek.entity.dto.doctor.DoctorRegistrationDTO;
import com.tek.entity.dto.user.UserLoginDTO;
import com.tek.entity.dto.user.UserRegistrationDTO;
import com.tek.exceptions.user.InvalidCredentialsException;
import com.tek.exceptions.user.UserNotExistsException;
import com.tek.repository.UserRepo;
import com.tek.service.DoctorService;
import com.tek.service.UserService;
import com.tek.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepo userRepo;

    private final UserService userService;

    private final DoctorService doctorService;

    private final JwtUtil jwtUtil;

    @PostMapping("/createUser")
    public ResponseEntity<ResponseDTO> createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.createUser(userRegistrationDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }

    @PostMapping("/userLogin")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        User user = userRepo.findByUserEmail(userLoginDTO.getUserEmail());

        if (Objects.isNull(user))
            throw new UserNotExistsException("User does not exists...");

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getUserEmail(), userLoginDTO.getUserPassword()));

            String token = jwtUtil.generateToken(userLoginDTO.getUserEmail(), user.getRole());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(UserConstants.STATUS_200, "Login successful", token));

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    @PostMapping("/createDoctor")
    public ResponseEntity<ResponseDTO> createDoctor(@Valid @RequestBody DoctorRegistrationDTO doctorRegistrationDTO) {
        doctorService.createDoctor(doctorRegistrationDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(DoctorConstants.STATUS_201, DoctorConstants.MESSAGE_201));
    }

    @PostMapping("/doctorLogin")
    public ResponseEntity<ResponseDTO> loginDoctor(@Valid @RequestBody DoctorLoginDTO doctorLoginDTO) {
        String msg = doctorService.doctorLogin(doctorLoginDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(DoctorConstants.STATUS_200, msg));
    }
}
