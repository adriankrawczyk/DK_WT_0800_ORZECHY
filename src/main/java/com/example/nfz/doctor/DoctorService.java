package com.example.nfz.doctor;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;


    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    public void onServiceStarted(){
        System.out.println("doctor service starting stuff...");
    }

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(int id) throws DoctorNotFoundException {
        return doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
    }

    public String deleteDoctorById(int id) throws DoctorNotFoundException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
        doctorRepository.delete(doctor);
        return "doctor has been deleted";
    }

    public Doctor saveDoctor(FormDoctorDTO doctor) {
        return doctorRepository.save(new Doctor(doctor.firstName(),doctor.lastName(),
                doctor.PESEL(),doctor.specialization(),doctor.street(),doctor.city(),doctor.zipcode()));
    }

    public void initDataBase(){
        doctorRepository.deleteAll();

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

        doctorRepository.saveAll(List.of(doctor1,doctor2,doctor3,doctor4,
                doctor5,doctor6,doctor7));
    }
}
