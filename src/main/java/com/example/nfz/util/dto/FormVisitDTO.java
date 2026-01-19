package com.example.nfz.util.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record FormVisitDTO(Integer scheduleId, LocalTime time, LocalDate date, Integer patientId) {
}
