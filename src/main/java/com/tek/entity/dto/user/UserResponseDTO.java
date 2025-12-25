package com.tek.entity.dto.user;

import com.tek.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {
    private Integer userId;
    private String userName;
    private Integer userAge;
    private String userEmail;
    private String phoneNumber;
    private String gender;
    private String role;
    private List<Appointment> appointment;
    private List<Comment> comment;
}
