package com.example.nfz;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Schedule;
import com.example.nfz.model.Specialization;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.service.OfficeService;
import com.example.nfz.util.dto.DetailedOfficeDTO;
import com.example.nfz.util.dto.FormOfficeDTO;
import com.example.nfz.util.dto.OfficeDTO;
import com.example.nfz.util.exceptions.IsBeingScheduledException;
import com.example.nfz.util.exceptions.OfficeAlreadyExistsException;
import com.example.nfz.util.exceptions.OfficeNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OfficeServiceTests {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeService officeService;

    Office testOffice = new Office(101);
    Office testOffice2 = new Office(202);
    OfficeDTO testOfficeDTO = new OfficeDTO(testOffice);
    FormOfficeDTO testOfficeForm = new FormOfficeDTO(101);

    @BeforeEach
    public void setup() {
    }

    @Test
    public void saveOfficeTest() throws OfficeAlreadyExistsException {
        when(officeRepository.save(any(Office.class))).thenReturn(testOffice);

        OfficeDTO savedOffice = officeService.saveOffice(testOfficeForm);
        Assertions.assertEquals(testOfficeDTO, savedOffice);
        verify(officeRepository).save(any(Office.class));
    }

    @Test
    public void saveOfficeWithDuplicateRoomNumberTest() {
        when(officeRepository.save(any(Office.class))).thenThrow(new DataIntegrityViolationException("Duplicate room number"));

        Assertions.assertThrows(OfficeAlreadyExistsException.class, () -> {
            officeService.saveOffice(testOfficeForm);
        });
    }

    @Test
    public void deleteOfficeTest() throws OfficeNotFoundException, IsBeingScheduledException {
        when(officeRepository.findById(1)).thenReturn(Optional.of(testOffice));

        officeService.deleteOfficeById(1);
        verify(officeRepository).findById(1);
        verify(officeRepository).delete(testOffice);
    }

    @Test
    public void deleteOfficeWithSchedulesTest() {
        Office officeWithSchedules = new Office(101);
        Doctor testDoctor = new Doctor("Ted", "Bundy", "1234567890",
                Specialization.CARDIOLOGY, "Sezamkowa", "Ameryka", "12-345");
        Schedule testSchedule = new Schedule(LocalTime.of(10, 0), LocalTime.of(11, 0),
                officeWithSchedules, testDoctor);
        officeWithSchedules.getSchedules().add(testSchedule);

        when(officeRepository.findById(1)).thenReturn(Optional.of(officeWithSchedules));

        Assertions.assertThrows(IsBeingScheduledException.class, () -> {
            officeService.deleteOfficeById(1);
        });

        verify(officeRepository).findById(1);
    }

    @Test
    public void deleteOfficeWithEmptyListTest() {
        when(officeRepository.findById(0)).thenReturn(Optional.empty());

        Assertions.assertThrows(OfficeNotFoundException.class, () -> {
            officeService.deleteOfficeById(0);
        });

        verify(officeRepository).findById(0);
    }

    @Test
    public void getOfficesTest() {
        when(officeRepository.findAll()).thenReturn(List.of(testOffice, testOffice2));

        List<Office> offices = officeService.getOffices();

        Assertions.assertNotNull(offices);
        Assertions.assertEquals(2, offices.size());
        Assertions.assertEquals(testOffice, offices.get(0));
        Assertions.assertEquals(testOffice2, offices.get(1));
    }

    @Test
    public void getOfficesWithEmptyListTest() {
        when(officeRepository.findAll()).thenReturn(List.of());

        List<Office> offices = officeService.getOffices();

        Assertions.assertNotNull(offices);
        Assertions.assertEquals(0, offices.size());
        verify(officeRepository).findAll();
    }

    @Test
    public void getOfficeByIdTest() throws OfficeNotFoundException {
        when(officeRepository.findById(1)).thenReturn(Optional.of(testOffice));

        Office foundOffice = officeService.getOfficeById(1);

        Assertions.assertEquals(testOffice, foundOffice);
        verify(officeRepository).findById(1);
    }

    @Test
    public void getOfficeByIdWithEmptyListTest() {
        when(officeRepository.findById(0)).thenReturn(Optional.empty());

        Assertions.assertThrows(OfficeNotFoundException.class, () -> {
            officeService.getOfficeById(0);
        });

        verify(officeRepository).findById(0);
    }

    @Test
    public void getOfficeDTOsTest() {
        when(officeRepository.findAll()).thenReturn(List.of(testOffice, testOffice2));

        List<OfficeDTO> officeDTOs = officeService.getOfficeDTOs();

        Assertions.assertNotNull(officeDTOs);
        Assertions.assertEquals(2, officeDTOs.size());
        Assertions.assertEquals(testOffice.getId(), officeDTOs.get(0).id());
        Assertions.assertEquals(testOffice.getRoomNumber(), officeDTOs.get(0).roomNumber());
        verify(officeRepository).findAll();
    }

    @Test
    public void getDetailedOfficeDTOByIdTest() throws OfficeNotFoundException {
        when(officeRepository.findById(1)).thenReturn(Optional.of(testOffice));

        DetailedOfficeDTO detailedDTO = officeService.getDetailedOfficeDtoById(1);
        Assertions.assertNotNull(detailedDTO);
        Assertions.assertEquals(testOffice.getId(), detailedDTO.id());
        Assertions.assertEquals(testOffice.getRoomNumber(), detailedDTO.roomNumber());
        verify(officeRepository).findById(1);
    }

    @Test
    public void getDetailedOfficeDTOByIdNotFoundTest() {
        when(officeRepository.findById(0)).thenReturn(Optional.empty());

        Assertions.assertThrows(OfficeNotFoundException.class, () -> {
            officeService.getDetailedOfficeDtoById(0);
        });

        verify(officeRepository).findById(0);
    }
}

