package com.example.nfz.util.dto;

import com.example.nfz.model.Patient;

public record PatientDTO(Integer id, String firstName, String lastName) {
    public PatientDTO(Patient patient) {
        this(patient.getId(), patient.getFirstName(), patient.getLastName());
    }
}
