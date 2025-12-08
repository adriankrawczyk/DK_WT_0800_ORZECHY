package com.example.nfz.doctor;

import com.example.nfz.util.Address;

public record DetailedDoctorDTO (Integer id,String firstName, String lastName,
                                 String specialization, Address address){
}
