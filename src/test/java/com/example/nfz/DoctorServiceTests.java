package com.example.nfz;

import com.example.nfz.doctor.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DoctorServiceTests {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;

    Doctor testDoctor = new Doctor("Ted","Bundy","1234567890","Patologia","Sezamkowa",
            "Ameryka","12-345");


    FormDoctorDTO testDoctorForm = new FormDoctorDTO(testDoctor);

    @BeforeEach
    public void setup() {
        doctorRepository.deleteAll();
    }


    @Test
    public void saveDoctorTest() {

        Doctor savedDoctor = doctorService.saveDoctor(testDoctorForm);

        Assertions.assertEquals(testDoctor,savedDoctor);

        Assertions.assertEquals(1,doctorRepository.count());
    }

    @Test
    public void deleteDoctorTest() {
        Doctor savedDoctor = doctorService.saveDoctor(testDoctorForm);

        try {
            doctorService.deleteDoctorById(savedDoctor.getId());
            Assertions.assertEquals(0, doctorRepository.count());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deleteDoctorWithEmptyListTest() {
        Assertions.assertThrows(DoctorNotFoundException.class, ()->{
            doctorService.deleteDoctorById(0);
        });
    }

    @Test
    public void getDoctorsTest() {
        Doctor testDoctor2 = new Doctor("Robute","Guilliman","400000000","Psychiatria",
                "Ultramarynowa 13", "Poznań", "42-000");

        FormDoctorDTO testDoctorForm2 = new FormDoctorDTO(testDoctor2);

        doctorService.saveDoctor(testDoctorForm);
        doctorService.saveDoctor(testDoctorForm2);

        List<Doctor> doctors = doctorService.getDoctors();

        Assertions.assertNotNull(doctors);
        Assertions.assertEquals(2,doctors.size());
        Assertions.assertEquals(testDoctor,doctors.get(0));
        Assertions.assertEquals(testDoctor2,doctors.get(1));
    }

    @Test
    public void getDoctorsWithEmptyListTest() {
        List<Doctor> doctors = doctorService.getDoctors();

        Assertions.assertNotNull(doctors);
        Assertions.assertEquals(0,doctors.size());
    }

    @Test
    public void getDoctorByIdTest() {
        Doctor savedDoctor = doctorService.saveDoctor(testDoctorForm);

        try {
            Doctor foundDoctor = doctorService.getDoctorById(savedDoctor.getId());

            Assertions.assertEquals(savedDoctor,foundDoctor);
        } catch (DoctorNotFoundException e) {
            System.out.println(e.getMessage());;
        }
    }

    @Test
    public void getDoctorByIdWithEmptyListTest() {
        Assertions.assertThrows(DoctorNotFoundException.class, ()->{
            doctorService.getDoctorById(0);
        });
    }

    @Test
    public void dataBaseInitTest() {

        //specializacja 1
        Doctor doctor1 = new Doctor("Gregory","House","1234567890",
                "Kardiologia","Tulipanowa 12","Kraków","05-345");

        Doctor doctor2 = new Doctor("Allison","Cameron","8888888888",
                "Kardiologia","Fiołkowa 3","Kraków","05-345");

        Doctor doctor3 = new Doctor("Robert","Chase","1298765430",
                "Kardiologia","Różana 8","Kraków","05-345");

        //specializacja 2
        Doctor doctor4 = new Doctor("Eric","Foreman","1234567899",
                "Neurologia","Narcyzowa 47","Kraków","05-345");

        Doctor doctor5 = new Doctor("Remy","Hadley","1313131313",
                "Neurologia","Chryzantemowa 13","Kraków","05-345");

        //specializacja 3
        Doctor doctor6 = new Doctor("James","Wilson","0987654321",
                "Onkologia","Bzowa 19","Kraków","05-345");

        //specializacja 4
        Doctor doctor7 = new Doctor("Lisa","Cudy","8280173827",
                "Urologia","Niezapominajkowa 45","Kraków","05-345");

        doctorService.initDataBase();

        List<Doctor> doctors = doctorService.getDoctors();
        Assertions.assertNotNull(doctors);

        Assertions.assertEquals(7,doctors.size());

        Assertions.assertEquals(doctor1,doctors.get(0));
        Assertions.assertEquals(doctor2,doctors.get(1));
        Assertions.assertEquals(doctor3,doctors.get(2));
        Assertions.assertEquals(doctor4,doctors.get(3));
        Assertions.assertEquals(doctor5,doctors.get(4));
        Assertions.assertEquals(doctor6,doctors.get(5));
        Assertions.assertEquals(doctor7,doctors.get(6));
    }
}
