package com.example.nfz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private int roomNumber;

    @OneToMany(mappedBy = "office")
    private List<Schedule> schedules = new ArrayList<>();

    public Office() {
    }

    public Office(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @JsonIgnore
    public List<Schedule> getSchedules() {
        return schedules;
    }

    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
