package com.example.nfz.model;

import jakarta.persistence.*;

@Entity
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private int roomNumber;

    public Office() {
    }

    public Office(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
