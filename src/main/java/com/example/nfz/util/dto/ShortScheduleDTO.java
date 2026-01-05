package com.example.nfz.util.dto;

import com.example.nfz.model.Schedule;

import java.time.LocalTime;

public record ShortScheduleDTO(Integer id, LocalTime startTime, LocalTime endTime) {
    public ShortScheduleDTO(Schedule schedule){
        this(schedule.getId(), schedule.getStartTime(), schedule.getEndTime());
    }
}
