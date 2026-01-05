package com.example.nfz.service;

import com.example.nfz.model.*;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.repository.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@Service
public class TestService {

    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final ScheduleRepository scheduleRepository;
    private final PatientRepository patientRepository;

    public TestService(DoctorRepository doctorRepository, OfficeRepository officeRepository, ScheduleRepository scheduleRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.scheduleRepository = scheduleRepository;
        this.patientRepository = patientRepository;
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
        scheduleRepository.deleteAll();
        scheduleRepository.flush();
        doctorRepository.deleteAll();
        doctorRepository.flush();
        officeRepository.deleteAll();
        officeRepository.flush();
        patientRepository.deleteAll();
        patientRepository.flush();

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

        Patient patient1 = new Patient("Jan", "Brzechwa", "0987654321",
                                    "Krokusowa 6", "Kraków", "05-345");

        Patient patient2 = new Patient("Olga", "Tokarczuk", "0648684321",
                                    "Magnoliowa 2", "Kraków", "05-345");

        Patient patient3 = new Patient("Adam", "Mickiewicz", "0987651111",
                                    "Żonkilowa 5", "Kraków", "05-345");

        patientRepository.saveAll(List.of(patient1, patient2, patient3));

        Office office1 = new Office(1);
        Office office2 = new Office(2);
        Office office3 = new Office(3);
        officeRepository.saveAll(List.of(office1, office2, office3));

        Schedule schedule1 = new Schedule(  LocalTime.of(11,30),
                                            LocalTime.of(12,30),
                                            office1,
                                            doctor1);

        office1.getSchedules().add(schedule1);
        doctor1.getSchedules().add(schedule1);

        scheduleRepository.save(schedule1);
        officeRepository.save(office1);
        doctorRepository.save(doctor1);


    }
}
