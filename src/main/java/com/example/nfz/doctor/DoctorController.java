package com.example.nfz.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.datatype.DatatypeConfigurationException;
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

        }catch(DoctorNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

}
