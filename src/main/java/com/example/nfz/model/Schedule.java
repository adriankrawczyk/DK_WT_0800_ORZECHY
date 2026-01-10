package com.example.nfz.model;

import com.example.nfz.util.dto.TimeSlotDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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

    @JsonIgnore
    public Doctor getDoctor() {
        return doctor;
    }

    @JsonIgnore
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(startTime, schedule.startTime) && Objects.equals(endTime, schedule.endTime) && Objects.equals(office, schedule.office) && Objects.equals(doctor, schedule.doctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, office, doctor);
    }

    public boolean collides(Schedule schedule) {
        //to samo
        if(this.equals(schedule)) return true;
        //jesli nie mają wspólnego lekarza i gabinetu, to nie koliduje
        else if(!(this.doctor.equals(schedule.getDoctor()) ||
                  this.office.equals(schedule.getOffice())  ) )return false;

        else if(this.startTime.equals(schedule.getStartTime() )) return true;
        else if(this.endTime.equals(schedule.getEndTime() )) return true;

        else if(this.startTime.isAfter(schedule.getStartTime()) &&
                this.startTime.isBefore(schedule.getEndTime())) return true;

        else if(this.endTime.isAfter(schedule.getStartTime()) &&
                this.endTime.isBefore(schedule.getEndTime())) return true;

        else if(schedule.getStartTime().isAfter(this.startTime) &&
                schedule.getStartTime().isBefore(this.endTime)) return true;

        else if(schedule.getEndTime().isAfter(this.startTime) &&
                schedule.getEndTime().isBefore(this.endTime)) return true;
        else return false;
    }

    public boolean collides(TimeSlotDTO schedule) {
        if(this.startTime.equals(schedule.startTime() )) return true;
        else if(this.endTime.equals(schedule.endTime() )) return true;

        else if(this.startTime.isAfter(schedule.startTime()) &&
                this.startTime.isBefore(schedule.endTime())) return true;

        else if(this.endTime.isAfter(schedule.startTime()) &&
                this.endTime.isBefore(schedule.endTime())) return true;

        else if(schedule.startTime().isAfter(this.startTime) &&
                schedule.startTime().isBefore(this.endTime)) return true;

        else if(schedule.endTime().isAfter(this.startTime) &&
                schedule.endTime().isBefore(this.endTime)) return true;
        else return false;
    }

    public boolean canMergeWith(Schedule schedule) {
        if(!(   this.doctor.equals(schedule.getDoctor()) &&
                this.office.equals(schedule.getOffice())  ) ) return false;

        if(this.startTime.equals(schedule.getEndTime()) || this.endTime.equals(schedule.getStartTime()) ) return true;
        else return false;
    }

    public static Schedule mergedFrom(List<Schedule> schedules){
        LocalTime minStartTime = LocalTime.MAX;
        LocalTime maxEndTime = LocalTime.MIN;

        for(Schedule schedule : schedules){
            if(schedule.getStartTime().isBefore(minStartTime))
                minStartTime = schedule.getStartTime();
            if(schedule.getEndTime().isAfter(maxEndTime))
                maxEndTime = schedule.getEndTime();
        }

        return new Schedule(minStartTime,maxEndTime,schedules.get(0).getOffice(),schedules.get(0).getDoctor());
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", office=" + office.getId()  +
                ", doctor=" + doctor.getId()  +
                '}';
    }
}
