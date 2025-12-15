package com.example.nfz.service;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Specialization;
import com.example.nfz.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final DoctorRepository doctorRepository;

    public TestService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    public void onServiceStarted(){
        System.out.println("test service starting stuff...");
    }



    public void initDataBase(){
        doctorRepository.deleteAll();

        //specializacja 1
        Doctor doctor1 = new Doctor("Gregory","House","1234567890",
                Specialization.CARDIOLOGY,"Tulipanowa 12","Kraków","05-345");

        Doctor doctor2 = new Doctor("Allison","Cameron","8888888888",
                Specialization.CARDIOLOGY,"Fiołkowa 3","Kraków","05-345");

        Doctor doctor3 = new Doctor("Robert","Chase","1298765430",
                Specialization.CARDIOLOGY,"Różana 8","Kraków","05-345");

        //specializacja 2
        Doctor doctor4 = new Doctor("Eric","Foreman","1234567899",
                Specialization.NEUROLOGY,"Narcyzowa 47","Kraków","05-345");

        Doctor doctor5 = new Doctor("Remy","Hadley","1313131313",
                Specialization.NEUROLOGY,"Chryzantemowa 13","Kraków","05-345");

        //specializacja 3
        Doctor doctor6 = new Doctor("James","Wilson","0987654321",
                Specialization.ONCOLOGY,"Bzowa 19","Kraków","05-345");

        //specializacja 4
        Doctor doctor7 = new Doctor("Lisa","Cudy","8280173827",
                Specialization.UROLOGY,"Niezapominajkowa 45","Kraków","05-345");

        doctorRepository.saveAll(List.of(doctor1,doctor2,doctor3,doctor4,
                doctor5,doctor6,doctor7));
    }
}
