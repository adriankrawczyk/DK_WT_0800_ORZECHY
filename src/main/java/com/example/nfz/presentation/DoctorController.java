package com.example.nfz.presentation;

import com.example.nfz.service.TestService;
import com.example.nfz.util.*;
import com.example.nfz.model.Doctor;
import com.example.nfz.service.DoctorService;
import com.example.nfz.util.dto.DetailedDoctorDTO;
import com.example.nfz.util.dto.DoctorDTO;
import com.example.nfz.util.dto.FormDoctorDTO;
import com.example.nfz.util.exceptions.DoctorNotFoundException;
import com.example.nfz.util.exceptions.IsBeingScheduledException;
import com.example.nfz.util.exceptions.SpecializationNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping(path = "doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final TestService  testService;

    public DoctorController(DoctorService doctorService, TestService testService) {
        this.doctorService = doctorService;
        this.testService = testService;
    }

    @GetMapping
    @Operation(
            summary = "get all doctors in the database",
            description = "returns id, full name and specialization of each doctor in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                        @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = DoctorDTO.class)
                            )
                        )
                    })
    })
    public List<DoctorDTO> getDoctors() {
        return doctorService.getDoctorDTOs();

    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get a specific doctor in the database",
            description = "returns id, full name, specialization and address of " +
                    "a specific doctor in the database based on the provided id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "doctor not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DetailedDoctorDTO.class)
                            )
                    })
    })
    public DetailedDoctorDTO getDoctor(@PathVariable String id) {
        try{
            return doctorService.getDetailedDoctorDTOById(Integer.parseInt(id));
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "doctor not found"),
            @ApiResponse(responseCode = "403", description = "doctor is being scheduled"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                        @Content(mediaType = "application/json",
                                schema = @Schema(implementation = InfoApiResponse.class)
                        )
                    }
            )
    })
    public ResponseEntity<InfoApiResponse> deleteDoctor(@PathVariable String id) {
        try{
            doctorService.deleteDoctorById(Integer.parseInt(id));
            return  ResponseEntity.ok(
                    new InfoApiResponse("doctor has been deleted")
            );
        }catch(DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found");
        } catch (IsBeingScheduledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "doctor is being scheduled");
        }
    }

    @PostMapping("/init")
    @Operation(
            summary = "initialize the database to a set state",
            description = "deletes all doctor records in the database and " +
                    "fills it with a set list of doctors"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InfoApiResponse.class)
                            )
                    }
            )
    })
    public ResponseEntity<InfoApiResponse> initDoctorDataBase() {
        testService.initDataBase();
        return ResponseEntity.ok(
                new InfoApiResponse("doctor list has been initialized"));
    }

    @PostMapping("/add")
    @Operation(
            summary = "adds a doctor to the database",
            description = "creates and adds a doctor to the database, " +
                    "returns created Doctor class object"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Doctor.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "specialization not found"),
    })
    public Doctor addDoctor(@RequestBody FormDoctorDTO doctor) {
        try {
            return doctorService.saveDoctor(doctor);
        } catch (SpecializationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
