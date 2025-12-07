package com.example.nfz.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping(path = "doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<DoctorDTO> getDoctors() {
        return doctorService.getDoctors().stream()
                .map(doctor -> new DoctorDTO(doctor.getId(),doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialization()))
                .toList();

    }

    @GetMapping("/{id}")
    public DetailedDoctorDTO getDoctor(@PathVariable String id) {
        try{
            Doctor doctor = doctorService.getDoctorById(Integer.parseInt(id));
            return new DetailedDoctorDTO(doctor.getId(),doctor.getFirstName(),doctor.getLastName(),
                    doctor.getSpecialization(),doctor.getAddress());

        }catch(DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable String id) {
        try{
           return doctorService.deleteDoctorById(Integer.parseInt(id));
        }catch(DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
