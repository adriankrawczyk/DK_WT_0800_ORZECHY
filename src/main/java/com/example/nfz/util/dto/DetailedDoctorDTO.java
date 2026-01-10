package com.example.nfz.util.dto;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Specialization;
import com.example.nfz.util.Address;

import java.util.List;

public record DetailedDoctorDTO (Integer id, String firstName, String lastName,
                                 Specialization specialization, Address address,
                                 List<DoctorScheduleDTO> schedules) {
    public DetailedDoctorDTO(Doctor doctor){
        this(
                doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                doctor.getSpecialization(), doctor.getAddress(),
                doctor.getSchedules().stream()
                        .map(DoctorScheduleDTO::new)
                        .toList()
        );
    }
}
