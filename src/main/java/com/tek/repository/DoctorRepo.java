package com.tek.repository;

import com.tek.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
    Doctor findByDoctorEmailAndPhoneNumber(String doctorEmail, String phoneNumber);

    Doctor findByDoctorEmail(String doctorEmail);

    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.doctorName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(d.specification) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchDoctors(String keyword);

}
