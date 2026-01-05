package com.example.nfz.util.dto;

import java.time.LocalTime;

public record FormScheduleDTO(LocalTime startTime, LocalTime endTime, Integer doctorId, Integer officeId) {
}
