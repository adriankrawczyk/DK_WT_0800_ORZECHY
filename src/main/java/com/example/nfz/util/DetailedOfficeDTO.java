package com.example.nfz.util;

import com.example.nfz.model.Office;

public record DetailedOfficeDTO(Integer id, Integer roomNumber) {
    public DetailedOfficeDTO(Office office){
        this(office.getId(), office.getRoomNumber());
    }
}
