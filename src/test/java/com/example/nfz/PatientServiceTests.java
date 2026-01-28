package com.example.nfz;

import com.example.nfz.model.Patient;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.service.PatientService;
import com.example.nfz.util.dto.DetailedPatientDTO;
import com.example.nfz.util.dto.FormPatientDTO;
import com.example.nfz.util.dto.PatientDTO;
import com.example.nfz.util.exceptions.PatientNotFoundException;
import com.example.nfz.util.exceptions.VisitScheduledException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;
    private Patient testPatient2;
    private FormPatientDTO testPatientForm;

    @BeforeEach
    public void setup() {
        testPatient = new Patient("Jan", "Kowalski", "12345678901", "Polna 1", "Warszawa", "00-001");
        testPatient2 = new Patient("Anna", "Nowak", "98765432109", "Lesna 5", "Krakow", "30-005");
        testPatientForm = new FormPatientDTO("Jan", "Kowalski", "12345678901", "Polna 1", "Warszawa", "00-001");
    }

    @Test
    public void getPatientsTest() {
        // pobranie listy wszystkich pacjentow
        when(patientRepository.findAll()).thenReturn(List.of(testPatient, testPatient2));

        List<Patient> patients = patientService.getPatients();

        Assertions.assertNotNull(patients);
        Assertions.assertEquals(2, patients.size());
        verify(patientRepository).findAll();
    }

    @Test
    public void getPatientDTOsTest() {
        // mapowanie listy pacjentow na dto
        when(patientRepository.findAll()).thenReturn(List.of(testPatient, testPatient2));

        List<PatientDTO> dtos = patientService.getPatientDTOs();

        Assertions.assertEquals(2, dtos.size());
        Assertions.assertEquals(testPatient.getFirstName(), dtos.get(0).firstName());
        verify(patientRepository).findAll();
    }

    @Test
    public void getPatientByIdTest() throws PatientNotFoundException {
        // znalezienie pacjenta po id
        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));

        Patient found = patientService.getPatientById(1);

        Assertions.assertEquals(testPatient, found);
        verify(patientRepository).findById(1);
    }

    @Test
    public void getPatientByIdNotFoundTest() {
        // blad gdy nie ma pacjenta o danym id
        when(patientRepository.findById(0)).thenReturn(Optional.empty());

        Assertions.assertThrows(PatientNotFoundException.class, () -> {
            patientService.getPatientById(0);
        });
    }

    @Test
    public void getDetailedPatientDTOByIdTest() throws PatientNotFoundException {
        // pobranie szczegolowych danych pacjenta
        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));

        DetailedPatientDTO dto = patientService.getDetailedPatientDTOById(1);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(testPatient.getPESEL(), dto.PESEL());
        verify(patientRepository).findById(1);
    }

    @Test
    public void savePatientTest() {
        // zapisanie nowego pacjenta do bazy
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        DetailedPatientDTO saved = patientService.savePatient(testPatientForm);

        Assertions.assertNotNull(saved);
        Assertions.assertEquals("Jan", saved.firstName());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    public void deletePatientTest() throws PatientNotFoundException, VisitScheduledException {
        // usuniecie pacjenta z bazy
        when(patientRepository.findById(1)).thenReturn(Optional.of(testPatient));

        patientService.deletePatientById(1);

        verify(patientRepository).findById(1);
        verify(patientRepository).delete(testPatient);
    }

    @Test
    public void deletePatientNotFoundTest() {
        // blad przy usuwaniu nieistniejacego pacjenta
        when(patientRepository.findById(99)).thenReturn(Optional.empty());

        Assertions.assertThrows(PatientNotFoundException.class, () -> {
            patientService.deletePatientById(99);
        });

        verify(patientRepository).findById(99);
    }
}