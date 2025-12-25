package com.tek.config;

import com.tek.entity.Doctor;
import com.tek.entity.User;
import com.tek.exceptions.UnauthorizedUserException;
import com.tek.exceptions.user.UserNotExistsException;
import com.tek.repository.DoctorRepo;
import com.tek.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    private final DoctorRepo doctorRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctor doctor = doctorRepo.findByDoctorEmail(username);
        if (Objects.nonNull(doctor))
            return doctor;

        User user = userRepo.findByUserEmail(username);
        if (Objects.nonNull(user))
            return user;

        throw  new UserNotExistsException("User not found");
    }
}
