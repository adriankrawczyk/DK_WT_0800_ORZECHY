package com.example.nfz.doctor;

public record FormDoctorDTO(String firstName, String lastName,
                            String PESEL, String specialization,  String street,
                            String city, String zipcode) {
    public FormDoctorDTO(Doctor doctor){
        this(doctor.getFirstName(), doctor.getLastName(), doctor.getPESEL(),
                doctor.getSpecialization(), doctor.getStreet(), doctor.getCity(), doctor.getZipCode());
    }
}
