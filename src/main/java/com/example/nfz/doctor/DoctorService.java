package com.example.nfz.doctor;

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

    public Doctor getDoctorById(int id) throws DoctorNotFoundException {
        return doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
    }
}
