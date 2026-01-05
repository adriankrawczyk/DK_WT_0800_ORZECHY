package com.example.nfz;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Schedule;
import com.example.nfz.model.Specialization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

public class ScheduleTests {

    Doctor doctor1 = new Doctor("Gregory", "House", "1234567890",
            Specialization.CARDIOLOGY, "Tulipanowa 12", "Kraków", "05-345");

    Doctor doctor2 = new Doctor("Allison", "Cameron", "8888888888",
            Specialization.CARDIOLOGY, "Fiołkowa 3", "Kraków", "05-345");

    Office office1 = new Office(1);
    Office office2 = new Office(2);

    LocalTime time1 = LocalTime.of(1,0,0);
    LocalTime time2 = LocalTime.of(2,0,0);

    @Test
    public void collisionTest(){
        Schedule schedule1 = new Schedule(time1,time2,office1,doctor1);
        Schedule schedule2 = new Schedule(time1,time2,office1,doctor1);

        Schedule schedule3 = new Schedule(time1,time2,office2,doctor1);
        Schedule schedule4 = new Schedule(time1,time2,office1,doctor2);

        Schedule schedule5 = new Schedule(time1,time2,office2,doctor2);


        Assertions.assertEquals(schedule2, schedule1);
        Assertions.assertTrue(schedule1.collides(schedule2));
        Assertions.assertTrue(schedule2.collides(schedule1));

        Assertions.assertTrue(schedule1.collides(schedule3));
        Assertions.assertTrue(schedule1.collides(schedule4));

        Assertions.assertFalse(schedule1.collides(schedule5));
    }
}
