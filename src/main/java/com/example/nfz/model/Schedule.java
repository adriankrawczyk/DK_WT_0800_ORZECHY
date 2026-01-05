package com.example.nfz.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    LocalTime startTime;
    LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public Schedule(LocalTime startTime, LocalTime endTime, Office office, Doctor doctor) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.office = office;
        this.doctor = doctor;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Office getOffice() {
        return office;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Schedule() {
    }

    public int getId() {
        return id;
    }
}
