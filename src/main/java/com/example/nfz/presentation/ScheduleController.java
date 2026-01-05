package com.example.nfz.presentation;

import com.example.nfz.service.ScheduleService;
import com.example.nfz.service.TestService;
import com.example.nfz.util.dto.DetailedScheduleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
