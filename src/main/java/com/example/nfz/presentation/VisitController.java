package com.example.nfz.presentation;

import com.example.nfz.repository.VisitRepository;
import com.example.nfz.service.VisitService;
import com.example.nfz.util.InfoApiResponse;
import com.example.nfz.util.dto.*;
import com.example.nfz.util.exceptions.*;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }


    @GetMapping
    @Operation(
            summary = "get all visits in the database",
            description = "returns time, doctor and patient details and id of schedule of every visit in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = VisitDTO.class)
                                    )
                            )
                    })
    })
    public List<VisitDTO> getVisits() {
        return visitService.getVisitsDTOs();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get a specific visit in the database",
            description = "returns time, doctor and patient details and id of schedule of a specific visit in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "visit not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VisitDTO.class)
                            )
                    })
    })
    public VisitDTO getVisitById(@PathVariable String id) {
        try{
            return visitService.getVisitDTOById(Integer.parseInt(id));
        }catch(VisitNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/avaliable")
    @Operation(
            summary = "get all available visit suggestions",
            description = "returns suggestions for visit times given doctor specialization and a time period"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "specialization not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = SuggestionVisitDTO.class)
                                    )
                            )
                    })
    })
    public List<SuggestionVisitDTO> getAvaliableVisits(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate,
                                                       @RequestParam String specialization) {
        try {
            return visitService.getAvaliableVisits(specialization, startDate, endDate);
        } catch (SpecializationNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    @Operation(
            summary = "adds a visit to the database",
            description = "creates and adds a visit to the database, " +
                    "returns created visit class object"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "patient or schedule not found"),
            @ApiResponse(responseCode = "403", description = "visit collision"),
            @ApiResponse(responseCode = "404", description = "invalid visit"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VisitDTO.class)
                            )
                    }
            ),
    })
    public VisitDTO addVisit(@RequestBody FormVisitDTO formVisitDTO) {
        try {
            VisitDTO visit = visitService.saveVisit(formVisitDTO);
            return visit;
        } catch (PatientNotFoundException | ScheduleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (VisitCollisionException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (InvalidvisitExcteption e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete a specific visit in the database",
            description = "deletes a specific visit in the database based on the provided id " +
                    "on successful deletion, returns \"visit has been deleted\""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "visit not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InfoApiResponse.class)
                            )
                    }
            )
    })
    public ResponseEntity<InfoApiResponse> deleteVisit(@PathVariable String id) {
        try{
            visitService.deleteVisitById(Integer.parseInt(id));
            return  ResponseEntity.ok(
                    new InfoApiResponse("visit has been deleted")
            );
        }catch(VisitNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "visit not found");
        }
    }

    @GetMapping("/patient/{id}")
    @Operation(
            summary = "get all visits of a patient in the database",
            description = "returns time, doctor and patient details and id of schedule of given patient's every visit in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = VisitDTO.class)
                                    )
                            )
                    })
    })
    public List<VisitDTO> getPatientVisits(@PathVariable String id) {
        return visitService.getPatientVisits(Integer.parseInt(id));
    }

    @GetMapping("/schedule/{id}")
    @Operation(
            summary = "get all visits of a schedule in the database",
            description = "returns time, doctor and patient details and id of schedule of given schedule's every visit in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = VisitDTO.class)
                                    )
                            )
                    })
    })
    public List<VisitDTO> getScheduleVisits(@PathVariable String id) {
        return visitService.getScheduleVisits(Integer.parseInt(id));
    }
}
