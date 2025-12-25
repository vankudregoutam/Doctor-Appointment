package com.tek.repository;

import com.tek.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {
    Appointment findByPhoneNumberAndProblem(String phoneNumber, String problem);

    List<Appointment> findByDoctor_DoctorId(Integer doctorId);

    List<Appointment> findByUser_UserId(Integer userId);
}
