package com.example.nfz.service;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Specialization;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.OfficeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {

    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;

    public TestService(DoctorRepository doctorRepository, OfficeRepository officeRepository) {
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
    }

    @PostConstruct
    private void onServiceStarted() {
        System.out.println("test service starting stuff...");
    }


    /**
     * initializes the database to a specific sta
     */
    @Transactional
    public void initDataBase() {
        doctorRepository.deleteAll();
        officeRepository.deleteAll();
        officeRepository.flush();

        //specializacja 1
        Doctor doctor1 = new Doctor("Gregory", "House", "1234567890",
                Specialization.CARDIOLOGY, "Tulipanowa 12", "Kraków", "05-345");

        Doctor doctor2 = new Doctor("Allison", "Cameron", "8888888888",
                Specialization.CARDIOLOGY, "Fiołkowa 3", "Kraków", "05-345");

        Doctor doctor3 = new Doctor("Robert", "Chase", "1298765430",
                Specialization.CARDIOLOGY, "Różana 8", "Kraków", "05-345");

        //specializacja 2
        Doctor doctor4 = new Doctor("Eric", "Foreman", "1234567899",
                Specialization.NEUROLOGY, "Narcyzowa 47", "Kraków", "05-345");

        Doctor doctor5 = new Doctor("Remy", "Hadley", "1313131313",
                Specialization.NEUROLOGY, "Chryzantemowa 13", "Kraków", "05-345");

        //specializacja 3
        Doctor doctor6 = new Doctor("James", "Wilson", "0987654321",
                Specialization.ONCOLOGY, "Bzowa 19", "Kraków", "05-345");

        //specializacja 4
        Doctor doctor7 = new Doctor("Lisa", "Cudy", "8280173827",
                Specialization.UROLOGY, "Niezapominajkowa 45", "Kraków", "05-345");

        doctorRepository.saveAll(List.of(doctor1, doctor2, doctor3, doctor4,
                doctor5, doctor6, doctor7));


        Office office1 = new Office(1);
        Office office2 = new Office(2);
        Office office3 = new Office(3);
        officeRepository.saveAll(List.of(office1, office2, office3));


    }
}
