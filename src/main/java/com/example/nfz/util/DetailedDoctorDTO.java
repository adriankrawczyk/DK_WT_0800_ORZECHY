package com.example.nfz.util;

import com.example.nfz.model.Doctor;

public record DetailedDoctorDTO (Integer id, String firstName, String lastName,
                                 String specialization, Address address){
    public DetailedDoctorDTO(Doctor doctor){
        this(
                doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                doctor.getSpecialization(), doctor.getAddress()
        );
    }
}
