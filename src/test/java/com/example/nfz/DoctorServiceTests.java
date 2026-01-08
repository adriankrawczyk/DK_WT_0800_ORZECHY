package com.example.nfz;

import com.example.nfz.model.Specialization;
import com.example.nfz.service.TestService;
import com.example.nfz.util.exceptions.DoctorNotFoundException;
import com.example.nfz.util.exceptions.IsBeingScheduledException;
import com.example.nfz.util.exceptions.SpecializationNotFoundException;
import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Schedule;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.service.DoctorService;
import com.example.nfz.util.dto.DetailedDoctorDTO;
import com.example.nfz.util.dto.DoctorDTO;
import com.example.nfz.util.dto.FormDoctorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringBootTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class DoctorServiceTests {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    @InjectMocks
    private TestService testService;


    Doctor testDoctor = new Doctor("Ted","Bundy","1234567890",Specialization.CARDIOLOGY,"Sezamkowa",
            "Ameryka","12-345");


    FormDoctorDTO testDoctorForm = new FormDoctorDTO(testDoctor);

    @BeforeEach
    public void setup() {
//        doctorRepository.deleteAll();
    }


    @Test
    public void saveDoctorTest() throws SpecializationNotFoundException {

        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        Doctor savedDoctor = doctorService.saveDoctor(testDoctorForm);
        Assertions.assertEquals(testDoctor,savedDoctor);
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    public void saveDoctorWithInvalidSpecializationTest() {
        FormDoctorDTO invalidForm = new FormDoctorDTO("John", "Doe", "1234567890", 
                "InvalidSpecialization", "Street", "City", "12-345");
        
        Assertions.assertThrows(SpecializationNotFoundException.class, () -> {
            doctorService.saveDoctor(invalidForm);
        });
    }

    @Test
    public void deleteDoctorTest() throws DoctorNotFoundException, IsBeingScheduledException {

        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));

        doctorService.deleteDoctorById(1);
//            Assertions.assertEquals(0, doctorRepository.count());
        verify(doctorRepository).findById(1);
        verify(doctorRepository).delete(testDoctor);
    }

    @Test
    public void deleteDoctorWithSchedulesTest() {
        Doctor doctorWithSchedules = new Doctor("Ted","Bundy","1234567890",
                Specialization.CARDIOLOGY,"Sezamkowa","Ameryka","12-345");
        Office testOffice = new Office(1);
        Schedule testSchedule = new Schedule(LocalTime.of(10, 0), LocalTime.of(11, 0), 
                testOffice, doctorWithSchedules);
        doctorWithSchedules.getSchedules().add(testSchedule);
        
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctorWithSchedules));

        Assertions.assertThrows(IsBeingScheduledException.class, () -> {
            doctorService.deleteDoctorById(1);
        });
        
        verify(doctorRepository).findById(1);
    }

    @Test
    public void deleteDoctorWithEmptyListTest() {
        when(doctorRepository.findById(0)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.deleteDoctorById(0);
        });
        
        verify(doctorRepository).findById(0);
    }

    @Test
    public void getDoctorsTest() {
        Doctor testDoctor2 = new Doctor("Robute","Guilliman","400000000", Specialization.NEUROLOGY,
                "Ultramarynowa 13", "Poznań", "42-000");

//        FormDoctorDTO testDoctorForm2 = new FormDoctorDTO(testDoctor2);
//
//        doctorService.saveDoctor(testDoctorForm);
//        doctorService.saveDoctor(testDoctorForm2);

        when(doctorRepository.findAll()).thenReturn(List.of(testDoctor,testDoctor2));

        List<Doctor> doctors = doctorService.getDoctors();

        Assertions.assertNotNull(doctors);
        Assertions.assertEquals(2,doctors.size());
        Assertions.assertEquals(testDoctor,doctors.get(0));
        Assertions.assertEquals(testDoctor2,doctors.get(1));
    }

    @Test
    public void getDoctorsWithEmptyListTest() {
        when(doctorRepository.findAll()).thenReturn(List.of());
        
        List<Doctor> doctors = doctorService.getDoctors();

        Assertions.assertNotNull(doctors);
        Assertions.assertEquals(0,doctors.size());
        verify(doctorRepository).findAll();
    }

    @Test
    public void getDoctorByIdTest() throws DoctorNotFoundException {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));
        
        Doctor foundDoctor = doctorService.getDoctorById(1);

        Assertions.assertEquals(testDoctor,foundDoctor);
        verify(doctorRepository).findById(1);
    }

    @Test
    public void getDoctorByIdWithEmptyListTest() {
        when(doctorRepository.findById(0)).thenReturn(Optional.empty());
        
        Assertions.assertThrows(DoctorNotFoundException.class, ()->{
            doctorService.getDoctorById(0);
        });
        
        verify(doctorRepository).findById(0);
    }

    @Test
    public void getDoctorDTOsTest() {
        Doctor testDoctor2 = new Doctor("Robute","Guilliman","400000000", Specialization.NEUROLOGY,
                "Ultramarynowa 13", "Poznań", "42-000");

        when(doctorRepository.findAll()).thenReturn(List.of(testDoctor, testDoctor2));

        List<DoctorDTO> doctorDTOs = doctorService.getDoctorDTOs();

        Assertions.assertNotNull(doctorDTOs);
        Assertions.assertEquals(2, doctorDTOs.size());
        Assertions.assertEquals(testDoctor.getId(), doctorDTOs.get(0).id());
        Assertions.assertEquals(testDoctor.getFirstName(), doctorDTOs.get(0).firstName());
        Assertions.assertEquals(testDoctor.getLastName(), doctorDTOs.get(0).lastName());
        Assertions.assertEquals(testDoctor.getSpecialization(), doctorDTOs.get(0).specialization());
        verify(doctorRepository).findAll();
    }

    @Test
    public void getDetailedDoctorDTOByIdTest() throws DoctorNotFoundException {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(testDoctor));

        DetailedDoctorDTO detailedDTO = doctorService.getDetailedDoctorDTOById(1);
        Assertions.assertNotNull(detailedDTO);
        Assertions.assertEquals(testDoctor.getId(), detailedDTO.id());
        Assertions.assertEquals(testDoctor.getFirstName(), detailedDTO.firstName());
        Assertions.assertEquals(testDoctor.getLastName(), detailedDTO.lastName());
        Assertions.assertEquals(testDoctor.getSpecialization(), detailedDTO.specialization());
        Assertions.assertEquals(testDoctor.getAddress(), detailedDTO.address());
        verify(doctorRepository).findById(1);
    }

    @Test
    public void getDetailedDoctorDTOByIdNotFoundTest() {
        when(doctorRepository.findById(0)).thenReturn(Optional.empty());

        Assertions.assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.getDetailedDoctorDTOById(0);
        });

        verify(doctorRepository).findById(0);
    }

    //Ten test będzie przeniesiony gdzies indziej (?)

//    @Test
//    public void dataBaseInitTest() {
//
//        //specializacja 1
//        Doctor doctor1 = new Doctor("Gregory","House","1234567890",
//                "Kardiologia","Tulipanowa 12","Kraków","05-345");
//
//        Doctor doctor2 = new Doctor("Allison","Cameron","8888888888",
//                "Kardiologia","Fiołkowa 3","Kraków","05-345");
//
//        Doctor doctor3 = new Doctor("Robert","Chase","1298765430",
//                "Kardiologia","Różana 8","Kraków","05-345");
//
//        //specializacja 2
//        Doctor doctor4 = new Doctor("Eric","Foreman","1234567899",
//                "Neurologia","Narcyzowa 47","Kraków","05-345");
//
//        Doctor doctor5 = new Doctor("Remy","Hadley","1313131313",
//                "Neurologia","Chryzantemowa 13","Kraków","05-345");
//
//        //specializacja 3
//        Doctor doctor6 = new Doctor("James","Wilson","0987654321",
//                "Onkologia","Bzowa 19","Kraków","05-345");
//
//        //specializacja 4
//        Doctor doctor7 = new Doctor("Lisa","Cudy","8280173827",
//                "Urologia","Niezapominajkowa 45","Kraków","05-345");
//
//        //tu nie mam pojęcia czy ma to sens
//        when(doctorRepository.findAll()).thenReturn(List.of(doctor1,doctor2,doctor3,doctor4,doctor5,doctor6,doctor7));
//
//        testService.initDataBase();
//
//        List<Doctor> doctors = doctorService.getDoctors();
//        Assertions.assertNotNull(doctors);
//
//        Assertions.assertEquals(7,doctors.size());
//
//        Assertions.assertEquals(doctor1,doctors.get(0));
//        Assertions.assertEquals(doctor2,doctors.get(1));
//        Assertions.assertEquals(doctor3,doctors.get(2));
//        Assertions.assertEquals(doctor4,doctors.get(3));
//        Assertions.assertEquals(doctor5,doctors.get(4));
//        Assertions.assertEquals(doctor6,doctors.get(5));
//        Assertions.assertEquals(doctor7,doctors.get(6));
//    }
}
