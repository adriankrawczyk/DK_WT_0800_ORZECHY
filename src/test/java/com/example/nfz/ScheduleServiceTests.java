package com.example.nfz;

import com.example.nfz.model.*;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.ScheduleRepository;
import com.example.nfz.service.ScheduleService;
import com.example.nfz.util.dto.*;
import com.example.nfz.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTests {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Doctor doctor;
    private Office office;

    private final LocalTime start = LocalTime.of(10, 0);
    private final LocalTime end = LocalTime.of(11, 0);

    @BeforeEach
    void setup() {
        // przygotowanie podstawowych danych testowych
        doctor = new Doctor("jan", "kowalski", "123",
                Specialization.CARDIOLOGY, "ulica", "miasto", "00-000");
        office = new Office(101);
    }

    @Test
    void shouldReturnAllSchedules() {
        // sprawdza pobranie listy grafikow
        Schedule s1 = new Schedule(start, end, office, doctor);
        when(scheduleRepository.findAll()).thenReturn(List.of(s1));

        List<Schedule> result = scheduleService.getSchedules();

        assertEquals(1, result.size());
        verify(scheduleRepository).findAll();
    }

    @Test
    void shouldReturnScheduleDtos() {
        // sprawdza mapowanie grafikow na dto
        Schedule s1 = new Schedule(start, end, office, doctor);
        when(scheduleRepository.findAll()).thenReturn(List.of(s1));

        List<DetailedScheduleDTO> result = scheduleService.getScheduleDTOs();

        assertEquals(1, result.size());
        verify(scheduleRepository).findAll();
    }

    @Test
    void shouldReturnFreeOffices() {
        // jedno biuro zajete, jedno wolne
        Office busyOffice = new Office(1);
        Office freeOffice = new Office(2);

        Schedule busy = new Schedule(start, end, busyOffice, doctor);
        busyOffice.getSchedules().add(busy);

        when(officeRepository.findAll()).thenReturn(List.of(busyOffice, freeOffice));

        TimeSlotDTO slot = new TimeSlotDTO(start, end);
        List<OfficeDTO> result = scheduleService.getFreeOffices(slot);

        assertEquals(1, result.size());
        assertEquals(freeOffice.getId(), result.get(0).id());
    }

    @Test
    void shouldReturnFreeDoctors() {
        // jeden lekarz zajety, drugi wolny
        Doctor busyDoctor = doctor;
        Doctor freeDoctor = new Doctor("anna", "nowak", "456",
                Specialization.NEUROLOGY, "x", "y", "11-111");

        Schedule busy = new Schedule(start, end, office, busyDoctor);
        busyDoctor.getSchedules().add(busy);

        when(doctorRepository.findAll()).thenReturn(List.of(busyDoctor, freeDoctor));

        TimeSlotDTO slot = new TimeSlotDTO(start, end);
        List<DoctorDTO> result = scheduleService.getFreeDoctors(slot);

        assertEquals(1, result.size());
        assertEquals(freeDoctor.getId(), result.get(0).id());
    }

    @Test
    void shouldSaveSchedule() throws Exception {
        // poprawne zapisanie grafiku
        FormScheduleDTO dto = new FormScheduleDTO(start, end, doctor.getId(), office.getId());

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(officeRepository.findById(office.getId())).thenReturn(Optional.of(office));
        when(scheduleRepository.save(any())).thenAnswer(a -> a.getArgument(0));

        DetailedScheduleDTO result = scheduleService.saveSchedule(dto);

        assertNotNull(result);
        assertEquals(start, result.startTime());
        verify(scheduleRepository).save(any());
    }

    @Test
    void shouldThrowWhenDoctorNotFound() {
        // brak lekarza w bazie
        FormScheduleDTO dto = new FormScheduleDTO(start, end, 999, office.getId());
        when(doctorRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,
                () -> scheduleService.saveSchedule(dto));
    }

    @Test
    void shouldThrowWhenOfficeNotFound() {
        // brak gabinetu w bazie
        FormScheduleDTO dto = new FormScheduleDTO(start, end, doctor.getId(), 999);

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(officeRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(OfficeNotFoundException.class,
                () -> scheduleService.saveSchedule(dto));
    }

    @Test
    void shouldThrowOnDoctorCollision() {
        // lekarz ma juz grafik w tym czasie
        Schedule existing = new Schedule(start, end, office, doctor);
        doctor.getSchedules().add(existing);

        FormScheduleDTO dto = new FormScheduleDTO(start, end, doctor.getId(), office.getId());

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(officeRepository.findById(office.getId())).thenReturn(Optional.of(office));

        assertThrows(ImpossibleScheduleException.class,
                () -> scheduleService.saveSchedule(dto));
    }

    @Test
    void shouldMergeNeighbourSchedules() throws Exception {
        // laczenie sasiadujacych grafikow
        Schedule existing = new Schedule(start, end, office, doctor);
        doctor.getSchedules().add(existing);
        office.getSchedules().add(existing);

        FormScheduleDTO dto = new FormScheduleDTO(end, LocalTime.of(12, 0),
                doctor.getId(), office.getId());

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(officeRepository.findById(office.getId())).thenReturn(Optional.of(office));
        when(scheduleRepository.save(any())).thenAnswer(a -> a.getArgument(0));

        DetailedScheduleDTO result = scheduleService.saveSchedule(dto);

        assertEquals(start, result.startTime());
        assertEquals(LocalTime.of(12, 0), result.endTime());
        verify(scheduleRepository).delete(existing);
    }

    @Test
    void shouldDeleteSchedule() throws Exception {
        // poprawne usuniecie grafiku
        Schedule schedule = new Schedule(start, end, office, doctor);
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(schedule));

        scheduleService.deleteScheduleById(1);

        verify(scheduleRepository).delete(schedule);
    }
}
