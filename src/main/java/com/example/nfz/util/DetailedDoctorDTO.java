package com.example.nfz.util;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Specialization;

public record DetailedDoctorDTO (Integer id, String firstName, String lastName,
                                 Specialization specialization, Address address){
    public DetailedDoctorDTO(Doctor doctor){
        this(
                doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                doctor.getSpecialization(), doctor.getAddress()
        );
    }
}
