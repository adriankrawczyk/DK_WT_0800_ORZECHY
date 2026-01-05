package com.example.nfz.service;

import com.example.nfz.model.Schedule;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.ScheduleRepository;
import com.example.nfz.util.dto.DetailedScheduleDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final OfficeRepository officeRepository;
    private final DoctorRepository doctorRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, OfficeRepository officeRepository, DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.officeRepository = officeRepository;
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    private void onServiceStarted() {
        System.out.println("schedule service starting stuff...");
    }

    /**
     * Returns the list of all schedules in the database
     *
     * @return list of found {@link Schedule}
     */
    public List<Schedule> getSchedules(){
        return scheduleRepository.findAll();
    }

    /**
     * Returns the list of all schedules in the database
     * and converts it to {@link DetailedScheduleDTO}
     *
     * @return list of found {@link Schedule}
     */
    public List<DetailedScheduleDTO> getScheduleDTOs(){
        return getSchedules().stream()
                .map(DetailedScheduleDTO::new)
                .toList();
    }



}
