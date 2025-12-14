package com.example.nfz.model;

import com.example.nfz.util.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstName;
    private String lastName;
    private String PESEL;
    private String specialization;
//    private String street;
//    private String city;
//    private String zipCode;
    @Embedded
    private Address address;

    public Doctor() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(firstName, doctor.firstName) && Objects.equals(lastName, doctor.lastName) && Objects.equals(PESEL, doctor.PESEL) && Objects.equals(specialization, doctor.specialization) && Objects.equals(address, doctor.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, PESEL, specialization, address);
    }

    public Doctor(String firstName, String lastName, String PESEL, String specialization, String street, String city, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.PESEL = PESEL;
        this.specialization = specialization;
        this.address = new Address(street, city, zipCode);
    }

    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    public String getZipCode() {
        return address.zipCode();
    }

    @JsonIgnore
    public String getCity() {
        return address.city();
    }

    @JsonIgnore
    public String getStreet() {
        return address.street();
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPESEL() {
        return PESEL;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
    public Address getAddress() {
        return address;
    }
}
