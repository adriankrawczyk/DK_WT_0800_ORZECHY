package com.example.nfz.util;

import com.example.nfz.model.Office;

public record OfficeDTO(Integer id, Integer roomNumber) {
    public OfficeDTO(Office office){
        this(office.getId(), office.getRoomNumber());
    }

}
