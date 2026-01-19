package com.example.nfz;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Specialization;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.service.DoctorService;
import com.example.nfz.service.TestService;
import com.example.nfz.util.InfoApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final TestService testService;

    public GreetingController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public String greeting() {
        return "<h1> Technologie obiektowe - projekt </h1>";
    }


    @PostConstruct
    public void onControllerCreated(){
        System.out.println("greeting controller starting stuff...");
        System.out.println("go to http://localhost:8080/swagger-ui/index.html to see the functionality");
    }

    @PostMapping("/init")
    @Operation(
            summary = "initialize the database to a set state",
            description = "all records in the database and " +
                    "fills it with a set data"
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
                new InfoApiResponse("database has been initialized"));
    }

}
