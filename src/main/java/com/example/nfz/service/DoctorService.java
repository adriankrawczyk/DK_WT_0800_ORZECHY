package com.example.nfz.service;

import com.example.nfz.model.Specialization;
import com.example.nfz.util.*;
import com.example.nfz.model.Doctor;
import com.example.nfz.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;


    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    public void onServiceStarted(){
        System.out.println("doctor service starting stuff...");
    }

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }


    public List<DoctorDTO> getDoctorDTOs(){
        return doctorRepository.findAll().stream()
            .map(DoctorDTO::new)
            .toList();
    }

    public Doctor getDoctorById(int id) throws DoctorNotFoundException {
        return doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
    }

    public DetailedDoctorDTO getDetailedDoctorDTOById(int id) throws DoctorNotFoundException {
        return new DetailedDoctorDTO(doctorRepository.findById(id)
                .orElseThrow(DoctorNotFoundException::new));
    }

    public void deleteDoctorById(int id) throws DoctorNotFoundException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
        doctorRepository.delete(doctor);
    }

    public Doctor saveDoctor(FormDoctorDTO doctor) throws SpecializationNotFoundException {
        Specialization specialization = Specialization.getSpecialization(doctor.specialization());

        return doctorRepository.save(new Doctor(doctor.firstName(),doctor.lastName(),
                doctor.PESEL(),specialization,doctor.street(),doctor.city(),doctor.zipcode()));
    }


}
