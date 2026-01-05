package com.example.nfz.util.dto;

import com.example.nfz.util.Address;

public record FormPatientDTO(String firstName, String lastName, String PESEL, String street, String city, String zipCode) {
}
