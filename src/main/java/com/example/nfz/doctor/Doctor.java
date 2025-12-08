package com.example.nfz.doctor;

import com.example.nfz.util.Address;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    private String street;
    private String city;
    private String zipCode;

    public Doctor() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(firstName, doctor.firstName) && Objects.equals(lastName, doctor.lastName) && Objects.equals(PESEL, doctor.PESEL) && Objects.equals(specialization, doctor.specialization) && Objects.equals(street, doctor.street) && Objects.equals(city, doctor.city) && Objects.equals(zipCode, doctor.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, PESEL, specialization, street, city, zipCode);
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
