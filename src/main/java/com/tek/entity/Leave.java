package com.tek.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Inheritance
@Table(name = "leave_request")
public class Leave extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leaveId")
    @SequenceGenerator(name = "leaveId", initialValue = 301, allocationSize = 1)
    private Integer leaveId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String leaveReason;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Doctor doctor;
}
