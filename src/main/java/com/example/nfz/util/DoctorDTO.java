package com.example.nfz.util;

import com.example.nfz.model.Doctor;

public record DoctorDTO(Integer id, String firstName, String lastName, String specialization) {
    public DoctorDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialization());
    }

}
