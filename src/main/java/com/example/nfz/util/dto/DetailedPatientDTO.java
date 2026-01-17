package com.example.nfz.util.dto;

import com.example.nfz.model.Patient;
import com.example.nfz.util.Address;

import java.time.LocalDate;

public record DetailedPatientDTO(Integer id, String firstName, String lastName,
                                 String PESEL, Address address) {

    public DetailedPatientDTO(Patient patient){
        this(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getPESEL(),
                patient.getAddress());
    }
}
