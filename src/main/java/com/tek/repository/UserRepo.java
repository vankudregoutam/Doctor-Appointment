package com.tek.repository;

import com.tek.entity.Role;
import com.tek.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    
    long countByRole(Role role);

    User findByUserEmail(String userEmail);

    User findByUserEmailAndPhoneNumber(String userEmail, String phoneNumber);

}
