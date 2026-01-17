package com.example.nfz.configuration;

import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.repository.ScheduleRepository;
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

    public DoctorEmptyConfigurator(DoctorRepository doctorRepository, ScheduleRepository scheduleRepository, OfficeRepository officeRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
        this.officeRepository = officeRepository;
        this.patientRepository = patientRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("DoctorEmptyConfigurator init");
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
