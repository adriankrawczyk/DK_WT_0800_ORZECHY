package com.example.nfz.presentation;

import com.example.nfz.util.DoctorNotFoundException;
import com.example.nfz.util.DetailedDoctorDTO;
import com.example.nfz.model.Doctor;
import com.example.nfz.util.DoctorDTO;
import com.example.nfz.util.FormDoctorDTO;
import com.example.nfz.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "get all doctors in the database",
            description = "returns id, full name and specialization of each doctor in the database"
    )
    public List<DoctorDTO> getDoctors() {
        return doctorService.getDoctors().stream()
                .map(DoctorDTO::new)
                .toList();

    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get a specific doctor in the database",
            description = "returns id, full name, specialization and address of " +
                    "a specific doctor in the database based on the provided id"
    )
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
    @Operation(
            summary = "delete a specific doctor in the database",
            description = "deletes a specific doctor in the database based on the provided id " +
                    "on successful deletion, returns \"doctor has been deleted\""
    )
    public String deleteDoctor(@PathVariable String id) {
        try{
           return doctorService.deleteDoctorById(Integer.parseInt(id));
        }catch(DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/init")
    @Operation(
            summary = "initialize the database to a set state",
            description = "deletes all doctor records in the database and " +
                    "fills it with a set list of doctors"
    )
    public String initDoctorDataBase() {
        doctorService.initDataBase();
        return "Successfully Initialized Doctor Data";
    }

    @PostMapping("/add")
    @Operation(
            summary = "adds a doctor to the database",
            description = "creates and adds a doctor to the database, " +
                    "returns created Doctor class object"
    )
    public Doctor addDoctor(@RequestBody FormDoctorDTO doctor) {
        return doctorService.saveDoctor(doctor);
    }
}
