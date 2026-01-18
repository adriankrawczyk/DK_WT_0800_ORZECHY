package com.example.nfz.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    LocalTime startTime;
    LocalTime endTime;

    public Visit(LocalTime startTime, LocalTime endTime, LocalDate date, Schedule schedule, Patient patient) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.schedule = schedule;
        this.patient = patient;
    }

    LocalDate date;

    public Visit() {

    }

    public int getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Patient getPatient() {
        return patient;
    }

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;
}
