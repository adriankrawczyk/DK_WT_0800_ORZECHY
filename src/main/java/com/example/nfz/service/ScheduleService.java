package com.example.nfz.service;

import com.example.nfz.model.Doctor;
import com.example.nfz.model.Office;
import com.example.nfz.model.Schedule;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.ScheduleRepository;
import com.example.nfz.util.exceptions.DoctorNotFoundException;
import com.example.nfz.util.exceptions.ImpossibleScheduleException;
import com.example.nfz.util.exceptions.OfficeNotFoundException;
import com.example.nfz.util.dto.DetailedScheduleDTO;
import com.example.nfz.util.dto.FormScheduleDTO;
import com.example.nfz.util.exceptions.ScheduleNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private boolean checkSchedulesCollisions(Schedule newSchedule, Set<Schedule> doctorSchedules,
                                            Set<Schedule> officeSchedules) {
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

    private List<Schedule> getMergableSchedules(Schedule targetSchedule, Set<Schedule> doctorSchedules) {
        List<Schedule> mergableSchedules = new ArrayList<>();
        for (Schedule schedule : doctorSchedules) {
            if(targetSchedule.canMergeWith(schedule)) {
                mergableSchedules.add(schedule);
            }
        }
        mergableSchedules.add(targetSchedule);
        for (Schedule schedule : mergableSchedules) {
            System.out.println(schedule);
        }
        return mergableSchedules;
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
     * Merges with existing schedules if available
     * @param formScheduleDTO form for adding a schedule
     * @return created {@link Schedule}
     * @throws DoctorNotFoundException if doctor does not exist
     * @throws OfficeNotFoundException  if office does not exist
     * @throws ImpossibleScheduleException if schedule collides with any other schedule or opening hours
     */
    @Transactional
    public Schedule saveSchedule(FormScheduleDTO formScheduleDTO) throws DoctorNotFoundException, OfficeNotFoundException, ImpossibleScheduleException {
        Doctor requestDoctor = doctorRepository.findById(formScheduleDTO.doctorId())
                .orElseThrow(DoctorNotFoundException::new);
        Office requestOffice = officeRepository.findById(formScheduleDTO.officeId())
                .orElseThrow(OfficeNotFoundException::new);

        Schedule newSchedule = new Schedule(formScheduleDTO.startTime(), formScheduleDTO.endTime(),
                requestOffice,requestDoctor);

        //kolizje godzinowe
        if(checkSchedulesCollisions(newSchedule,requestDoctor.getSchedules(), requestOffice.getSchedules()))
            throw new ImpossibleScheduleException();

        //znajdz możliwe do połączenia dyżury
        List<Schedule> mergableSchedules = getMergableSchedules(newSchedule, requestDoctor.getSchedules());

        //jesli są, połacz
        if(mergableSchedules.size()>1)
            newSchedule = Schedule.mergedFrom(mergableSchedules);



        //Żeby dyżur trwał co najm godzinę
        Duration duration = Duration.between(newSchedule.getStartTime(), newSchedule.getEndTime());
        if(duration.toHours()<1) throw new ImpossibleScheduleException();

        //usun zbedne dyżury
        for(Schedule schedule : mergableSchedules) {
            requestOffice.getSchedules().remove(schedule);
            requestDoctor.getSchedules().add(schedule);

            scheduleRepository.delete(schedule);
        }


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
