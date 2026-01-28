package com.example.nfz.model;

import com.example.nfz.util.dto.SuggestionVisitDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalTime startTime;
    private LocalTime endTime;

    private final static Set<Integer> possibleVisitMinutes = Set.of(0,15,30,45);
    private final static long visitDuration = 15;

    public Visit(LocalTime startTime,  LocalDate date, Schedule schedule, Patient patient) {
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(visitDuration);
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

    public boolean collides(SuggestionVisitDTO suggestionVisitDTO) {
        if (suggestionVisitDTO.scheduleId() != this.schedule.getId()) return false;
        if(!suggestionVisitDTO.date().equals(this.date)) return false;
        return this.startTime.equals(suggestionVisitDTO.time());
    }

    public boolean collides(Visit visit) {
        if(visit.getSchedule().getId() != this.schedule.getId()) return false;
        if(visit.getPatient().getId() != this.patient.getId()) return false;
        if(!visit.getDate().equals(this.date)) return false;
        return this.startTime.equals(visit.getStartTime());
    }

    public boolean valid() {
        //czy zaczyna sie na kwadransie
        if (!possibleVisitMinutes.contains(this.getStartTime().getMinute())) return false;
        //czy miesci sie w przedziale dyżuru
        if (this.startTime.isBefore(this.schedule.getStartTime()) ||
                this.startTime.isAfter(this.schedule.getEndTime()) ||
                this.endTime.isBefore(this.schedule.getStartTime()) ||
                this.endTime.isAfter(this.schedule.getEndTime())) return false;
        return !this.date.isBefore(LocalDate.now());

    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "schedule=" + schedule.getId() +
                ", date=" + date +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", id=" + id +
                '}';
    }
}


