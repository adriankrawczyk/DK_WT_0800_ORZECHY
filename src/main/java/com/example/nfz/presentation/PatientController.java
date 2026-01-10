package com.example.nfz.presentation;

import com.example.nfz.model.Office;
import com.example.nfz.model.Patient;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.service.PatientService;
import com.example.nfz.util.InfoApiResponse;
import com.example.nfz.util.dto.*;
import com.example.nfz.util.exceptions.IsBeingScheduledException;
import com.example.nfz.util.exceptions.OfficeAlreadyExistsException;
import com.example.nfz.util.exceptions.OfficeNotFoundException;
import com.example.nfz.util.exceptions.PatientNotFoundException;
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
@RequestMapping(path = "patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final PatientService patientService;

    public PatientController(PatientRepository patientRepository, PatientService patientService) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }


    @GetMapping
    @Operation(
            summary = "get all patients in the database",
            description = "returns id and full name of every patient in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PatientDTO.class)
                                    )
                            )
                    })
    })
    public List<PatientDTO> getPatients() {
        return patientService.getPatientDTOs();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get a specific patient in the database",
            description = "returns full information of" +
                    "a specific patient in the database based on the provided id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DetailedPatientDTO.class)
                            )
                    })
    })
    public DetailedPatientDTO getPatient(@PathVariable String id) {
        try{
            return patientService.getDetailedPatientDTOById(Integer.parseInt(id));
        }catch(PatientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"patient not found");
        }

    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete a specific patient in the database",
            description = "deletes a specific patient in the database based on the provided id " +
                    "on successful deletion, returns \"office has been deleted\""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InfoApiResponse.class)
                            )
                    }
            )
    })
    public ResponseEntity<InfoApiResponse> deletePatient(@PathVariable String id) {
        try {
            patientService.deletePatientById(Integer.parseInt(id));
            return ResponseEntity.ok(
                    new InfoApiResponse("patient has been deleted")
            );
        } catch (PatientNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "patient not found");
        }
    }

    @PostMapping("/add")
    @Operation(
            summary = "adds a patient to the database",
            description = "creates and adds a patient to the database, " +
                    "returns created Patient class object"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Patient.class)
                            )
                    }
            ),
    })
    public Patient addPatient(@RequestBody FormPatientDTO patient) {
            return patientService.savePatient(patient);
    }

}
