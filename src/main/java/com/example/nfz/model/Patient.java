package com.example.nfz.model;

import com.example.nfz.util.Address;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstName;
    private String lastName;
    private String PESEL;

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits = new ArrayList<>();

    @Embedded
    private Address address;

    //może wiek (data urodzenia),  płeć


    public Patient(String firstName, String lastName, String PESEL, String street, String city, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.PESEL = PESEL;
        this.address = new Address(street, city, zipCode);
    }



    public Patient() {
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPESEL() {
        return PESEL;
    }

    public Address getAddress() {
        return address;
    }

}
