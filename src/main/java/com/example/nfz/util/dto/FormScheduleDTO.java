package com.example.nfz.util.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record FormScheduleDTO(@JsonFormat(pattern = "HH:mm:ss") LocalTime startTime,@JsonFormat(pattern = "HH:mm:ss") LocalTime endTime, Integer doctorId, Integer officeId) {
}
