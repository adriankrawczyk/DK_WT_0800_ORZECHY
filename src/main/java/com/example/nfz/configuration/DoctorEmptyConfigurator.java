package com.example.nfz.configuration;

import com.example.nfz.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("empty")
public class DoctorEmptyConfigurator {

    private final DoctorRepository doctorRepository;

    public DoctorEmptyConfigurator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("DoctorEmptyConfigurator init");
        if (doctorRepository.count() != 0) {
            doctorRepository.deleteAll();
        }
    }
}
