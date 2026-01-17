package com.example.nfz.util.dto;

import com.example.nfz.model.Schedule;

import java.time.LocalTime;

public record DetailedScheduleDTO(Integer id, LocalTime startTime, LocalTime endTime,
                                  Integer doctorId, Integer officeId) {
    public DetailedScheduleDTO(Schedule schedule){
        this(schedule.getId(), schedule.getStartTime(), schedule.getEndTime(),
                schedule.getDoctor().getId(), schedule.getOffice().getId());
    }


}
