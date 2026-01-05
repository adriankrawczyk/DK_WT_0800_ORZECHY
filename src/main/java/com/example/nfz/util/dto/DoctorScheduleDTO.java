package com.example.nfz.util.dto;

import com.example.nfz.model.Schedule;

import java.time.LocalTime;

public record DoctorScheduleDTO(Integer id, LocalTime startTime, LocalTime endTime,
                               OfficeDTO office) {
    public DoctorScheduleDTO(Schedule schedule) {
        this(schedule.getId(), schedule.getStartTime(),schedule.getEndTime(),
                new OfficeDTO(schedule.getOffice()));
    }
}
