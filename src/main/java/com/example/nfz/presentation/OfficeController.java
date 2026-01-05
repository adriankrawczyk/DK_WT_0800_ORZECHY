package com.example.nfz.presentation;

import com.example.nfz.model.Office;
import com.example.nfz.service.OfficeService;
import com.example.nfz.service.TestService;
import com.example.nfz.util.*;
import com.example.nfz.util.dto.DetailedOfficeDTO;
import com.example.nfz.util.dto.FormOfficeDTO;
import com.example.nfz.util.dto.OfficeDTO;
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
@RequestMapping(path = "offices")
public class OfficeController {
    private final OfficeService officeService;
    private final TestService testService;

    public OfficeController(OfficeService officeService, TestService testService) {
        this.officeService = officeService;

        this.testService = testService;
    }

    @GetMapping
    @Operation(
            summary = "get all offices in the database",
            description = "returns id and room number of every office in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = OfficeDTO.class)
                                    )
                            )
                    })
    })
    public List<OfficeDTO> getOffices() {
        return officeService.getOfficeDTOs();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "get a specific office in the database",
            description = "returns id and room number of" +
                    "a specific office in the database based on the provided id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "office not found"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DetailedOfficeDTO.class)
                            )
                    })
    })
    public DetailedOfficeDTO getOffice(@PathVariable String id) {
        try{
            return officeService.getDetailedOfficeDtoById(Integer.parseInt(id));
        }catch(OfficeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"office not found");
        }

    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete a specific office in the database",
            description = "deletes a specific office in the database based on the provided id " +
                    "on successful deletion, returns \"office has been deleted\""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "office not found"),
            @ApiResponse(responseCode = "403", description = "office is being scheduled"),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InfoApiResponse.class)
                            )
                    }
            )
    })
    public ResponseEntity<InfoApiResponse> deleteOffice(@PathVariable String id) {
        try{
            officeService.deleteOfficeById(Integer.parseInt(id));
            return  ResponseEntity.ok(
                    new InfoApiResponse("office has been deleted")
            );
        }catch(OfficeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "office not found");
        } catch (IsBeingScheduledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "office is being scheduled");
        }
    }

    @PostMapping("/add")
    @Operation(
            summary = "adds an office to the database",
            description = "creates and adds an office to the database, " +
                    "returns created Office class object"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content ={
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Office.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "409", description = "office with this room number already exists"),
    })
    public Office addOffice(@RequestBody FormOfficeDTO office) {
        try {
            return officeService.saveOffice(office);
        } catch (OfficeAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"office already exists");
        }
    }




}
