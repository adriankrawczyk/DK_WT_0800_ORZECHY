package com.example.nfz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private int roomNumber;

    @OneToMany(mappedBy = "office")
    private Set<Schedule> schedules = new HashSet<>();

    public Office() {
    }

    public Office(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @JsonIgnore
    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Office office = (Office) o;
        return roomNumber == office.roomNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomNumber);
    }
}
