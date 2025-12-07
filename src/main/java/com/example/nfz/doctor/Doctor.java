package com.example.nfz.doctor;

import com.example.nfz.util.Address;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstName;
    private String lastName;
    private String PESEL;
    private String specialization;
    private String street;
    private String city;
    private String zipCode;

    public Doctor() {

    }

    public Doctor(String firstName, String lastName, String PESEL, String specialization, String street, String city, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.PESEL = PESEL;
        this.specialization = specialization;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getLastName() {
        return lastName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
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
        return new Address(street, city, zipCode);
    }
}
