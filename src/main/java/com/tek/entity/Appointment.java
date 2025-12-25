package com.tek.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Inheritance
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointmentId")
    @SequenceGenerator(name = "appointmentId", initialValue = 401, allocationSize = 1)
    private Integer appointmentId;
    private String problem;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String patientName;
    private Integer patientAge;
    private String phoneNumber;
    private String patientEmail;
    private String patientGender;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Doctor doctor;

}
