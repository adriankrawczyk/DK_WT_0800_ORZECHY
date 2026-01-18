package com.example.nfz.configuration;

import com.example.nfz.repository.*;
import com.example.nfz.service.DoctorService;
import com.example.nfz.service.ScheduleService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("empty")
public class DoctorEmptyConfigurator {

    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;
    private final OfficeRepository officeRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public DoctorEmptyConfigurator(DoctorRepository doctorRepository, ScheduleRepository scheduleRepository, OfficeRepository officeRepository, PatientRepository patientRepository, VisitRepository visitRepository) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
        this.officeRepository = officeRepository;
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("DoctorEmptyConfigurator init");
        if(visitRepository.count() != 0){
            visitRepository.deleteAll();
        }
        if(scheduleRepository.count() != 0){
            scheduleRepository.deleteAll();
        }
        if (doctorRepository.count() != 0) {
            doctorRepository.deleteAll();
        }
        if (officeRepository.count() != 0) {
            officeRepository.deleteAll();
        }

        if (patientRepository.count() != 0) {
            patientRepository.deleteAll();
        }
    }
}
