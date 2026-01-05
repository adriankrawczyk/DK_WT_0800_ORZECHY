package com.example.nfz.util.dto;

import com.example.nfz.model.Office;

import java.util.List;

public record DetailedOfficeDTO(Integer id, Integer roomNumber, List<OfficeScheduleDTO> schedules) {
    public DetailedOfficeDTO(Office office){
        this(office.getId(), office.getRoomNumber(),
                office.getSchedules().stream()
                        .map(OfficeScheduleDTO::new)
                        .toList());
    }
}
