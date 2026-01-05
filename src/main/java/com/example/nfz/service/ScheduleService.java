package com.example.nfz.service;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Schedule;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.ScheduleRepository;
import com.example.nfz.util.DoctorNotFoundException;
import com.example.nfz.util.ImpossibleScheduleException;
import com.example.nfz.util.OfficeNotFoundException;
import com.example.nfz.util.dto.DetailedScheduleDTO;
import com.example.nfz.util.dto.FormScheduleDTO;
import com.example.nfz.util.dto.ScheduleNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final OfficeRepository officeRepository;
    private final DoctorRepository doctorRepository;

    //godziny otwarcia
    private static final LocalTime minStartTime = LocalTime.of(7,0);
    private static final LocalTime maxEndTime = LocalTime.of(18,0);

    public ScheduleService(ScheduleRepository scheduleRepository, OfficeRepository officeRepository, DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.officeRepository = officeRepository;
        this.doctorRepository = doctorRepository;
    }

    private boolean checkSchedulesCollisions(Schedule newSchedule, List<Schedule> doctorSchedules,
                                            List<Schedule> officeSchedules) {
        if(newSchedule.getStartTime().isBefore(minStartTime) ||
                newSchedule.getEndTime().isAfter(maxEndTime)) return true;

        for (Schedule schedule : doctorSchedules) {
            if(newSchedule.collides(schedule)) return true;
        }
        for (Schedule schedule : officeSchedules) {
            if(newSchedule.collides(schedule)) return true;
        }
        return false;
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


    /**
     * Adds a new schedule to database
     * @param formScheduleDTO form for adding a schedule
     * @return created {@link Schedule}
     * @throws DoctorNotFoundException if doctor does not exists
     * @throws OfficeNotFoundException  if office does not exists
     * @throws ImpossibleScheduleException if schedule collides with any other schedule or opening hours
     */
    @Transactional
    public Schedule saveSchedule(FormScheduleDTO formScheduleDTO) throws DoctorNotFoundException, OfficeNotFoundException, ImpossibleScheduleException {
        Doctor requestDoctor = doctorRepository.findById(formScheduleDTO.doctorId())
                .orElseThrow(DoctorNotFoundException::new);
        Office requestOffice = officeRepository.findById(formScheduleDTO.officeId())
                .orElseThrow(OfficeNotFoundException::new);

        //Żeby dyżur trwał co najm godzinę
        Duration duration = Duration.between(formScheduleDTO.startTime(), formScheduleDTO.endTime());
        if(duration.toHours()<1) throw new ImpossibleScheduleException();

        Schedule newSchedule = new Schedule(formScheduleDTO.startTime(), formScheduleDTO.endTime(),
                requestOffice,requestDoctor);

        if(checkSchedulesCollisions(newSchedule,requestDoctor.getSchedules(), requestOffice.getSchedules()))
            throw new ImpossibleScheduleException();

        scheduleRepository.save(newSchedule);

        requestOffice.getSchedules().add(newSchedule);
        officeRepository.save(requestOffice);

        requestDoctor.getSchedules().add(newSchedule);
        doctorRepository.save(requestDoctor);

        return newSchedule;
    }

    /**
     *
     * @param scheduleId
     * @return found {@link Schedule}
     * @throws ScheduleNotFoundException if schedulne does not exist
     */
    public Schedule getScheduleById(int scheduleId) throws ScheduleNotFoundException {
        return scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
    }


    public void deleteScheduleById(int scheduleId) throws ScheduleNotFoundException {
        Schedule schedule = getScheduleById(scheduleId);
        scheduleRepository.delete(schedule);
    }

}
