package com.example.nfz.util;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Specialization;

public record DoctorDTO(Integer id, String firstName, String lastName, Specialization specialization) {
    public DoctorDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialization());
    }

}
