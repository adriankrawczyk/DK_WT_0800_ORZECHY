package com.example.nfz.util.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SuggestionVisitDTO(LocalDate date, LocalTime time, DoctorDTO doctor) {
}
