package com.example.nfz;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Schedule;
import com.example.nfz.model.Specialization;
import com.example.nfz.util.dto.TimeSlotDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

public class ScheduleTests {

    Doctor doctor1 = new Doctor("Gregory", "House", "1234567890",
            Specialization.CARDIOLOGY, "Tulipanowa 12", "Kraków", "05-345");

    Doctor doctor2 = new Doctor("Allison", "Cameron", "8888888888",
            Specialization.CARDIOLOGY, "Fiołkowa 3", "Kraków", "05-345");

    Office office1 = new Office(1);
    Office office2 = new Office(2);

    LocalTime time1 = LocalTime.of(1,0,0);
    LocalTime time2 = LocalTime.of(2,0,0);
    LocalTime time3 = LocalTime.of(3,0,0);
    LocalTime time4 = LocalTime.of(4,0,0);

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

    @Test
    public void canMergeSchedulesTest(){
        Schedule schedule1 = new Schedule(time1,time2,office1,doctor1);
        Schedule schedule2 = new Schedule(time1,time2,office1,doctor1);
        Schedule schedule3 = new Schedule(time2,time3,office1,doctor1);
        Schedule schedule4 = new Schedule(time3,time4,office1,doctor1);
        Schedule schedule5 = new Schedule(time2,time3,office1,doctor2);

        Assertions.assertTrue(schedule1.canMergeWith(schedule3));
        Assertions.assertTrue(schedule3.canMergeWith(schedule1));

        Assertions.assertFalse(schedule1.canMergeWith(schedule4));
        Assertions.assertFalse(schedule1.canMergeWith(schedule2));

        Assertions.assertTrue(schedule3.canMergeWith(schedule4));

        Assertions.assertFalse(schedule1.canMergeWith(schedule5));

    }

    @Test
    public void mergeTest(){
        Schedule schedule1 = new Schedule(time1,time2,office1,doctor1);
        Schedule schedule2 = new Schedule(time2,time3,office1,doctor1);
        Schedule schedule3 = new Schedule(time3,time4,office1,doctor1);

        Schedule schedule4 = new Schedule(time1,time3,office1,doctor1);
        Schedule schedule5 = new Schedule(time1,time4,office1,doctor1);

        Schedule mergedSchedule1 = Schedule.mergedFrom(List.of(schedule1,schedule2));
        Schedule mergedSchedule2 = Schedule.mergedFrom(List.of(schedule3,schedule2,schedule1));

        Assertions.assertEquals(mergedSchedule1, schedule4);
        Assertions.assertEquals(mergedSchedule2, schedule5);
    }

    @Test
    public void collisionEdgeCasesTest() {
        // sasiednie grafiki bez nakladania sie
        Schedule schedule1 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule2 = new Schedule(time2, time3, office1, doctor1);
        Assertions.assertFalse(schedule1.collides(schedule2));

        // jeden grafik zawiera drugi
        Schedule schedule3 = new Schedule(time1, time4, office1, doctor1);
        Schedule schedule4 = new Schedule(time2, time3, office1, doctor1);
        Assertions.assertTrue(schedule3.collides(schedule4));
        Assertions.assertTrue(schedule4.collides(schedule3));

        // calkowicie rozlaczne grafiki
        Schedule schedule5 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule6 = new Schedule(time3, time4, office1, doctor1);
        Assertions.assertFalse(schedule5.collides(schedule6));

        // nakladanie na poczatku
        Schedule schedule7 = new Schedule(time1, time3, office1, doctor1);
        Schedule schedule8 = new Schedule(time2, time4, office1, doctor1);
        Assertions.assertTrue(schedule7.collides(schedule8));

        // nakladanie na koncu
        Schedule schedule9 = new Schedule(time2, time4, office1, doctor1);
        Schedule schedule10 = new Schedule(time1, time3, office1, doctor1);
        Assertions.assertTrue(schedule9.collides(schedule10));
    }

    @Test
    public void collisionWithTimeSlotDTOTest() {
        Schedule schedule1 = new Schedule(time1, time2, office1, doctor1);
        TimeSlotDTO timeSlot1 = new TimeSlotDTO(time1, time2);
        TimeSlotDTO timeSlot2 = new TimeSlotDTO(time2, time3);
        TimeSlotDTO timeSlot3 = new TimeSlotDTO(time3, time4);
        TimeSlotDTO timeSlot4 = new TimeSlotDTO(LocalTime.of(0, 30), LocalTime.of(1, 30));

        // dokladne dopasowanie
        Assertions.assertTrue(schedule1.collides(timeSlot1));

        // sasiednie bez nakladania
        Assertions.assertFalse(schedule1.collides(timeSlot2));

        // calkowicie rozlaczne
        Assertions.assertFalse(schedule1.collides(timeSlot3));

        // nakladanie sie
        Assertions.assertTrue(schedule1.collides(timeSlot4));

        // timeslot zawiera grafik
        TimeSlotDTO timeSlot5 = new TimeSlotDTO(LocalTime.of(0, 30), LocalTime.of(2, 30));
        Assertions.assertTrue(schedule1.collides(timeSlot5));

        // grafik zawiera timeslot
        TimeSlotDTO timeSlot6 = new TimeSlotDTO(LocalTime.of(1, 15), LocalTime.of(1, 45));
        Assertions.assertTrue(schedule1.collides(timeSlot6));
    }

    @Test
    public void equalsAndHashCodeTest() {
        Schedule schedule1 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule2 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule3 = new Schedule(time2, time3, office1, doctor1);
        Schedule schedule4 = new Schedule(time1, time2, office2, doctor1);
        Schedule schedule5 = new Schedule(time1, time2, office1, doctor2);

        // identyczne grafiki
        Assertions.assertEquals(schedule1, schedule2);
        Assertions.assertEquals(schedule1.hashCode(), schedule2.hashCode());

        // rozny czas
        Assertions.assertNotEquals(schedule1, schedule3);

        // rozny gabinet
        Assertions.assertNotEquals(schedule1, schedule4);

        // rozny lekarz
        Assertions.assertNotEquals(schedule1, schedule5);

        // null
        Assertions.assertNotEquals(schedule1, null);

        // rozny typ
        Assertions.assertNotEquals(schedule1, "not a schedule");

        // wlasciwosc refleksywna
        Assertions.assertEquals(schedule1, schedule1);

        // wlasciwosc symetryczna
        Assertions.assertEquals(schedule1, schedule2);
        Assertions.assertEquals(schedule2, schedule1);
    }

    @Test
    public void mergeEdgeCasesTest() {
        Schedule schedule1 = new Schedule(time1, time2, office1, doctor1);

        // polaczenie pojedynczego grafiku
        Schedule mergedSingle = Schedule.mergedFrom(List.of(schedule1));
        Assertions.assertEquals(schedule1.getStartTime(), mergedSingle.getStartTime());
        Assertions.assertEquals(schedule1.getEndTime(), mergedSingle.getEndTime());
        Assertions.assertEquals(schedule1.getOffice(), mergedSingle.getOffice());
        Assertions.assertEquals(schedule1.getDoctor(), mergedSingle.getDoctor());

        // polaczenie z nakladajacymi sie czasami
        Schedule schedule2 = new Schedule(time1, time3, office1, doctor1);
        Schedule schedule3 = new Schedule(time2, time4, office1, doctor1);
        Schedule mergedOverlap = Schedule.mergedFrom(List.of(schedule2, schedule3));
        Assertions.assertEquals(time1, mergedOverlap.getStartTime());
        Assertions.assertEquals(time4, mergedOverlap.getEndTime());

        // polaczenie z nieposortowanej listy
        Schedule schedule4 = new Schedule(time3, time4, office1, doctor1);
        Schedule schedule5 = new Schedule(time1, time2, office1, doctor1);
        Schedule mergedUnsorted = Schedule.mergedFrom(List.of(schedule4, schedule5));
        Assertions.assertEquals(time1, mergedUnsorted.getStartTime());
        Assertions.assertEquals(time4, mergedUnsorted.getEndTime());
    }

    @Test
    public void canMergeEdgeCasesTest() {
        // sasiednie grafiki koniec pierwszego = start drugiego
        Schedule schedule1 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule2 = new Schedule(time2, time3, office1, doctor1);
        Assertions.assertTrue(schedule1.canMergeWith(schedule2));

        // sasiednie grafiki start pierwszego = koniec drugiego
        Schedule schedule3 = new Schedule(time2, time3, office1, doctor1);
        Schedule schedule4 = new Schedule(time1, time2, office1, doctor1);
        Assertions.assertTrue(schedule3.canMergeWith(schedule4));

        // ten sam lekarz i gabinet ale nie sasiednie
        Schedule schedule5 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule6 = new Schedule(time3, time4, office1, doctor1);
        Assertions.assertFalse(schedule5.canMergeWith(schedule6));

        // rozny lekarz ten sam gabinet
        Schedule schedule7 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule8 = new Schedule(time2, time3, office1, doctor2);
        Assertions.assertFalse(schedule7.canMergeWith(schedule8));

        // ten sam lekarz rozny gabinet
        Schedule schedule9 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule10 = new Schedule(time2, time3, office2, doctor1);
        Assertions.assertFalse(schedule9.canMergeWith(schedule10));
    }

    @Test
    public void boundaryConditionsTest() {
        // grafiki o polnocy
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalTime oneAM = LocalTime.of(1, 0);
        Schedule schedule1 = new Schedule(midnight, oneAM, office1, doctor1);
        Schedule schedule2 = new Schedule(oneAM, LocalTime.of(2, 0), office1, doctor1);
        Assertions.assertFalse(schedule1.collides(schedule2));
        Assertions.assertTrue(schedule1.canMergeWith(schedule2));

        // grafiki na koncu dnia
        LocalTime endOfDay = LocalTime.of(23, 59);
        LocalTime beforeEnd = LocalTime.of(23, 0);
        Schedule schedule3 = new Schedule(beforeEnd, endOfDay, office1, doctor1);
        Schedule schedule4 = new Schedule(endOfDay, LocalTime.of(23, 59, 59), office1, doctor1);
        Assertions.assertFalse(schedule3.collides(schedule4));    // 
        Assertions.assertTrue(schedule3.canMergeWith(schedule4)); // zakladamy ze nakladanie sie jest dozwolone

        // bardzo krotki czas
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(10, 1);
        Schedule schedule5 = new Schedule(start, end, office1, doctor1);
        Schedule schedule6 = new Schedule(end, LocalTime.of(10, 2), office1, doctor1);
        Assertions.assertFalse(schedule5.collides(schedule6));
        Assertions.assertTrue(schedule5.canMergeWith(schedule6));
    }

    @Test
    public void getterTest() {
        Schedule schedule = new Schedule(time1, time2, office1, doctor1);

        Assertions.assertEquals(time1, schedule.getStartTime());
        Assertions.assertEquals(time2, schedule.getEndTime());
        Assertions.assertEquals(office1, schedule.getOffice());
        Assertions.assertEquals(doctor1, schedule.getDoctor());
    }

    @Test
    public void toStringTest() {
        Schedule schedule = new Schedule(time1, time2, office1, doctor1);
        String toString = schedule.toString();

        Assertions.assertNotNull(toString);
        Assertions.assertTrue(toString.contains("Schedule"));
        Assertions.assertTrue(toString.contains("startTime"));
        Assertions.assertTrue(toString.contains("endTime"));
    }

    @Test
    public void collisionWithSameTimeDifferentResourcesTest() {
        // ten sam czas i lekarz rozny gabinet
        Schedule schedule1 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule2 = new Schedule(time1, time2, office2, doctor1);
        Assertions.assertTrue(schedule1.collides(schedule2));

        // ten sam czas i gabinet rozny lekarz
        Schedule schedule3 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule4 = new Schedule(time1, time2, office1, doctor2);
        Assertions.assertTrue(schedule3.collides(schedule4));

        // ten sam czas rozne gabinet i lekarz
        Schedule schedule5 = new Schedule(time1, time2, office1, doctor1);
        Schedule schedule6 = new Schedule(time1, time2, office2, doctor2);
        Assertions.assertFalse(schedule5.collides(schedule6));
    }
}
