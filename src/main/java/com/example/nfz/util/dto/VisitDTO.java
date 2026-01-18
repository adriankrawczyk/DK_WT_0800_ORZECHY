package com.example.nfz.util.dto;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Visit;

import java.time.LocalDate;
import java.time.LocalTime;

public record VisitDTO(Integer id,LocalDate date, LocalTime time, DoctorDTO doctor, PatientDTO patient) {
    public VisitDTO(Visit visit){
        this(visit.getId() ,visit.getDate(),visit.getStartTime(),
                new DoctorDTO(visit.getSchedule().getDoctor()),
                new PatientDTO(visit.getPatient()));
    }
}
