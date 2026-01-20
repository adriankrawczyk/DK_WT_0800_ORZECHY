package com.example.nfz;

import com.example.nfz.model.*;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.repository.ScheduleRepository;
import com.example.nfz.repository.VisitRepository;
import com.example.nfz.service.VisitService;
import com.example.nfz.util.dto.FormVisitDTO;
import com.example.nfz.util.dto.SuggestionVisitDTO;
import com.example.nfz.util.dto.VisitDTO;
import com.example.nfz.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTests {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private VisitService visitService;

    private Patient testPatient;
    private Doctor testDoctor;
    private Office testOffice;
    private Schedule testSchedule;
    private Visit testVisit;

    @BeforeEach
    void setup() {
        // przygotowanie podstawowych danych testowych
        testPatient = new Patient("Jan", "Kowalski", "12345678901",
                "Polna 1", "Warszawa", "00-001");
        testDoctor = new Doctor("Gregory", "House", "1234567890",
                Specialization.CARDIOLOGY, "Tulipanowa 12", "Kraków", "05-345");
        testOffice = new Office(1);
        testSchedule = new Schedule(LocalTime.of(10, 0), LocalTime.of(12, 0),
                testOffice, testDoctor);
        testVisit = new Visit(LocalTime.of(10, 30), LocalDate.now(),
                testSchedule, testPatient);
    }

    @Test
    void shouldReturnAllVisitDTOs() {
        // pobranie listy wszystkich wizyt jako dto
        when(visitRepository.findAll()).thenReturn(List.of(testVisit));

        List<VisitDTO> result = visitService.getVisitsDTOs();

        assertEquals(1, result.size());
        assertEquals(testPatient.getFirstName(), result.get(0).patient().firstName());
        verify(visitRepository).findAll();
    }

    @Test
    void shouldReturnVisitDTOById() throws VisitNotFoundException {
        // znalezienie wizyty po id
        when(visitRepository.findById(1)).thenReturn(Optional.of(testVisit));

        VisitDTO result = visitService.getVisitDTOById(1);

        assertNotNull(result);
        assertEquals(testVisit.getStartTime(), result.time());
        verify(visitRepository).findById(1);
    }

    @Test
    void shouldThrowWhenVisitNotFound() {
        // blad gdy nie ma wizyty o danym id
        when(visitRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(VisitNotFoundException.class,
                () -> visitService.getVisitDTOById(999));

        verify(visitRepository).findById(999);
    }

    @Test
    void shouldReturnAvailableVisits() throws SpecializationNotFoundException {
        // znalezienie dostepnych terminow wizyt
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(5);

        when(scheduleRepository.findAll()).thenReturn(List.of(testSchedule));

        List<SuggestionVisitDTO> result = visitService.getAvaliableVisits(
                "KARDIOLOGIA", start, end);

        assertNotNull(result);
        verify(scheduleRepository).findAll();
    }

    @Test
    void shouldThrowOnInvalidSpecialization() {
        // blad przy niepoprawnej specjalizacji
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(5);

        assertThrows(SpecializationNotFoundException.class,
                () -> visitService.getAvaliableVisits("INVALID_SPEC", start, end));
    }

    @Test
    void shouldReturnEmptyListForInvalidDateRange() throws SpecializationNotFoundException {
        // pusta lista gdy zakres dat jest niepoprawny
        LocalDate start = LocalDate.now().plusDays(5);
        LocalDate end = LocalDate.now().plusDays(1);

        List<SuggestionVisitDTO> result = visitService.getAvaliableVisits(
                "KARDIOLOGIA", start, end);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveVisit() throws Exception {
        // poprawne zapisanie wizyty
        FormVisitDTO formDto = new FormVisitDTO(testSchedule.getId(),
                LocalTime.of(11, 0), LocalDate.now().plusDays(1), testPatient.getId());

        when(patientRepository.findById(testPatient.getId()))
                .thenReturn(Optional.of(testPatient));
        when(scheduleRepository.findById(testSchedule.getId()))
                .thenReturn(Optional.of(testSchedule));
        when(visitRepository.save(any(Visit.class))).thenAnswer(a -> a.getArgument(0));

        VisitDTO result = visitService.saveVisit(formDto);

        assertNotNull(result);
        assertEquals(formDto.time(), result.time());
        verify(visitRepository).save(any(Visit.class));
    }

    @Test
    void shouldThrowWhenPatientNotFoundOnSave() {
        // blad gdy nie ma pacjenta w bazie
        FormVisitDTO formDto = new FormVisitDTO(testSchedule.getId(),
                LocalTime.of(11, 0), LocalDate.now().plusDays(1), 999);

        when(patientRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> visitService.saveVisit(formDto));
    }

    @Test
    void shouldThrowWhenScheduleNotFoundOnSave() {
        // blad gdy nie ma grafiku w bazie
        FormVisitDTO formDto = new FormVisitDTO(999,
                LocalTime.of(11, 0), LocalDate.now().plusDays(1), testPatient.getId());

        when(patientRepository.findById(testPatient.getId()))
                .thenReturn(Optional.of(testPatient));
        when(scheduleRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ScheduleNotFoundException.class,
                () -> visitService.saveVisit(formDto));
    }

    @Test
    void shouldThrowOnVisitCollision() {
        // blad przy kolizji wizyt
        Visit existingVisit = new Visit(LocalTime.of(11, 0), LocalDate.now().plusDays(1),
                testSchedule, testPatient);
        testPatient.getVisits().add(existingVisit);

        FormVisitDTO formDto = new FormVisitDTO(testSchedule.getId(),
                LocalTime.of(11, 5), LocalDate.now().plusDays(1), testPatient.getId());

        when(patientRepository.findById(testPatient.getId()))
                .thenReturn(Optional.of(testPatient));
        when(scheduleRepository.findById(testSchedule.getId()))
                .thenReturn(Optional.of(testSchedule));

        assertThrows(InvalidvisitExcteption.class,
                () -> visitService.saveVisit(formDto));
    }

    @Test
    void shouldDeleteVisitById() throws VisitNotFoundException {
        // poprawne usuniecie wizyty
        when(visitRepository.findById(1)).thenReturn(Optional.of(testVisit));

        visitService.deleteVisitById(1);

        verify(visitRepository).findById(1);
        verify(visitRepository).delete(testVisit);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentVisit() {
        // blad przy usuwaniu nieistniejącej wizyty
        when(visitRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(VisitNotFoundException.class,
                () -> visitService.deleteVisitById(999));

        verify(visitRepository).findById(999);
    }

    @Test
    void shouldReturnPatientVisits() {
        // pobranie wszystkich wizyt pacjenta
        when(visitRepository.findAllByPatient_Id(testPatient.getId()))
                .thenReturn(List.of(testVisit));

        List<VisitDTO> result = visitService.getPatientVisits(testPatient.getId());

        assertEquals(1, result.size());
        assertEquals(testVisit.getStartTime(), result.get(0).time());
        verify(visitRepository).findAllByPatient_Id(testPatient.getId());
    }

    @Test
    void shouldReturnScheduleVisits() {
        // pobranie wszystkich wizyt dla grafiku
        when(visitRepository.findAllBySchedule_Id(testSchedule.getId()))
                .thenReturn(List.of(testVisit));

        List<VisitDTO> result = visitService.getScheduleVisits(testSchedule.getId());

        assertEquals(1, result.size());
        assertEquals(testVisit.getStartTime(), result.get(0).time());
        verify(visitRepository).findAllBySchedule_Id(testSchedule.getId());
    }

    @Test
    void shouldFilterOutWeekendsInDateRange() throws SpecializationNotFoundException {
        // filtrowanie weekendow z zakresu dat
        LocalDate monday = LocalDate.of(2026, 1, 26);
        LocalDate sunday = LocalDate.of(2026, 2, 1);

        when(scheduleRepository.findAll()).thenReturn(List.of(testSchedule));

        List<SuggestionVisitDTO> result = visitService.getAvaliableVisits(
                "KARDIOLOGIA", monday, sunday);

        // sprawdz ze zadna sugestia nie wypada w weekend
        assertTrue(result.stream().noneMatch(v ->
                v.date().getDayOfWeek().name().equals("SATURDAY") ||
                v.date().getDayOfWeek().name().equals("SUNDAY")));
    }
}
