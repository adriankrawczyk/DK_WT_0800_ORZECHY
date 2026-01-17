package com.example.nfz.repository;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findAllDoctorsBySpecialization(Specialization specialization);
}
