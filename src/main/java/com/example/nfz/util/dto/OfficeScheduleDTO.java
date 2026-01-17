package com.example.nfz.util.dto;

import com.example.nfz.model.Schedule;

import java.time.LocalTime;

public record OfficeScheduleDTO(Integer id, LocalTime startTime, LocalTime endTime,
                                DoctorDTO doctor) {
    public OfficeScheduleDTO(Schedule schedule) {
        this(schedule.getId(), schedule.getStartTime(),schedule.getEndTime(),
               new DoctorDTO(schedule.getDoctor()) );
    }
}
