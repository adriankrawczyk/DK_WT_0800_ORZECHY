package com.example.nfz.presentation;

import com.example.nfz.model.Office;
import com.example.nfz.model.Schedule;
import com.example.nfz.service.ScheduleService;
import com.example.nfz.service.TestService;
import com.example.nfz.util.*;
import com.example.nfz.util.dto.DetailedScheduleDTO;
import com.example.nfz.util.dto.FormOfficeDTO;
import com.example.nfz.util.dto.FormScheduleDTO;
import com.example.nfz.util.dto.ScheduleNotFoundException;
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
@RequestMapping(path = "schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final TestService testService;

    public ScheduleController(ScheduleService scheduleService, TestService testService) {
        this.scheduleService = scheduleService;
        this.testService = testService;
    }


    @GetMapping
    @Operation(
            summary = "get all schedules in the database",
            description = "returns id, start time, end time, id of doctor and office of the schedules in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = DetailedScheduleDTO.class)
                                    )
                            )
                    })
    })
    public List<DetailedScheduleDTO> getSchedules() {
        return scheduleService.getScheduleDTOs();
    }

    @PostMapping("/add")
    @Operation(
            summary = "adds a schedule to the database",
            description = "creates and adds a schedule to the database, " +
                    "returns created Schedule class object"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Schedule.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "400", description = "schedule collides with other schedules or opening hours or is too short"),
            @ApiResponse(responseCode = "404", description = "doctor or office not found")
    })
    public Schedule sddSchedule(@RequestBody FormScheduleDTO schedule) {
        try {
            return scheduleService.saveSchedule(schedule);
        } catch (OfficeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "office not found");
        } catch (DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found");
        } catch (ImpossibleScheduleException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "schedule collision");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete a specific schedule in the database",
            description = "deletes a specific schedule in the database based on the provided id " +
                    "on successful deletion, returns \"schedule has been deleted\""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "schedule not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InfoApiResponse.class)
                            )
                    }
            )
    })
    public ResponseEntity<InfoApiResponse> deleteSchedule(@PathVariable String id) {
        try{
            scheduleService.deleteScheduleById(Integer.parseInt(id));
            return  ResponseEntity.ok(
                    new InfoApiResponse("schedule has been deleted")
            );
        }catch(ScheduleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "schedule not found");
        }
    }
}
