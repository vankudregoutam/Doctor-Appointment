package com.tek.repository;

import com.tek.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepo extends JpaRepository<Leave, Integer> {
    @Query("SELECT COUNT(l) > 0 FROM Leave l WHERE l.doctor.doctorId = :doctorId " +
            "AND ((l.fromDate <= :toDate AND l.toDate >= :fromDate))")
    boolean existsByDoctor_DoctorIdAndDateOverlap(Integer doctorId, LocalDate fromDate, LocalDate toDate);

    List<Leave> findByDoctor_DoctorId(Integer doctorId);
}
