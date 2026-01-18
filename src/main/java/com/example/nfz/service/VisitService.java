package com.example.nfz.service;

import com.example.nfz.model.Patient;
import com.example.nfz.model.Schedule;
import com.example.nfz.model.Specialization;
import com.example.nfz.model.Visit;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.repository.ScheduleRepository;
import com.example.nfz.repository.VisitRepository;
import com.example.nfz.util.dto.DoctorDTO;
import com.example.nfz.util.dto.FormVisitDTO;
import com.example.nfz.util.dto.SuggestionVisitDTO;
import com.example.nfz.util.dto.VisitDTO;
import com.example.nfz.util.exceptions.*;
import jdk.jshell.SourceCodeAnalysis;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;

    private final long visitDuration =  15;

    public VisitService(VisitRepository visitRepository, PatientRepository patientRepository, ScheduleRepository scheduleRepository) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.scheduleRepository = scheduleRepository;
    }

    private LocalTime findFirstQuadrant(LocalTime time){
        if (time.getMinute()>45) return time.withHour(time.getHour()+1).withMinute(0);
        else if (time.getMinute()>30) return time.withMinute(45);
        else if (time.getMinute()>15) return time.withMinute(30);
        else return time.withMinute(15);
    }

    private List<LocalDate> getDatesRange(LocalDate start, LocalDate end){
        List<LocalDate> dates = new ArrayList<>();
//        if(!start.isAfter(end) && start.getDayOfWeek()!= DayOfWeek.SATURDAY && start.getDayOfWeek()!= DayOfWeek.SUNDAY)
            dates.add(start);
        LocalDate date = start.plusDays(1);
        while(!date.isAfter(end) && date.getDayOfWeek()!= DayOfWeek.SATURDAY && date.getDayOfWeek()!= DayOfWeek.SUNDAY
                && !date.isBefore(LocalDate.now())) {
            dates.add(date);
            date = date.plusDays(1);
        }
        return dates;
    }

    private List<SuggestionVisitDTO> extractVisitSugetsions(List<Schedule> schedules, List<LocalDate> dates) {
        List<SuggestionVisitDTO> visits = new ArrayList<>();

        for (Schedule schedule : schedules) {
            //we start visits always at a quadrant
            LocalTime startTime = findFirstQuadrant(schedule.getStartTime());
            System.out.println(startTime);
            while(!startTime.plusMinutes(visitDuration).isAfter(schedule.getEndTime())){
                System.out.println(startTime);
                for(LocalDate date : dates){
                    visits.add(new SuggestionVisitDTO(date,startTime,
                                                        new DoctorDTO(schedule.getDoctor()),
                                                        schedule.getId()));
                }
                startTime = startTime.plusMinutes(visitDuration);
            }
        }

        return visits;
    }


    private List<Visit> getVisits() {
        return visitRepository.findAll();
    }

    /**
     *
     * @return
     */
    public List<VisitDTO> getVisitsDTOs() {
        return getVisits().stream()
                .map(VisitDTO::new)
                .toList();
    }

    private Visit getVisitById(int id) throws VisitNotFoundException {
        return visitRepository.findById(id).orElseThrow(VisitNotFoundException::new);
    }

    /**
     *
     * @param id
     * @return
     * @throws VisitNotFoundException
     */
    public VisitDTO getVisitDTOById(int id) throws VisitNotFoundException {
        return new VisitDTO(getVisitById(id));
    }

    /**
     *
     * @param spec specialization
     * @param startDate
     * @param endDate
     * @return list of avaliable visit suggestion
     * @throws SpecializationNotFoundException if specialization not found
     */
    @Transactional
    public List<SuggestionVisitDTO> getAvaliableVisits(String spec, LocalDate startDate, LocalDate endDate) throws SpecializationNotFoundException {

        Specialization specialization = Specialization.getSpecialization(spec);

        if(endDate.isBefore(startDate) || startDate.isBefore(LocalDate.now())){
            return new ArrayList<>();
        }

        List<LocalDate> dateRange = getDatesRange(startDate, endDate);
        System.out.println("dateRange: " + dateRange);

        //find schedules with matching doctors
        List<Schedule> specializationSchedules = scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getDoctor().getSpecialization() == specialization)
                .toList();

        List<Visit> possibleCollisonVisits = specializationSchedules.stream()
                .flatMap(schedule ->schedule.getVisits().stream())
                .filter(visit -> !visit.getDate().isAfter(endDate) && !visit.getDate().isBefore(startDate))
                .toList();

        //extract possible visits

        List<SuggestionVisitDTO> suggestions = extractVisitSugetsions(specializationSchedules,dateRange);

        //remove colisions

        for (Visit visit : possibleCollisonVisits) {
            suggestions.removeIf(visit::collides);
        }

        return suggestions;
    }

    @Transactional
    public VisitDTO saveVisit(FormVisitDTO formVisitDTO) throws PatientNotFoundException, ScheduleNotFoundException, VisitCollisionException, InvalidvisitExcteption {
        Patient patient = patientRepository.findById(formVisitDTO.patientId()).orElseThrow(PatientNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(formVisitDTO.scheduleId()).orElseThrow(ScheduleNotFoundException::new);

        Visit newVisit = new Visit(formVisitDTO.time(),
                formVisitDTO.date(), schedule, patient);
        //spawdz poprawnośc wizyty
        if(!newVisit.valid()) throw new InvalidvisitExcteption();

        //sprawdz czy koliduje z innymi wizytami
        for(Visit patientVisit: patient.getVisits()){
            if(patientVisit.collides(newVisit)) throw new VisitCollisionException();
        }

        for(Visit scheduleVisit: schedule.getVisits()){
            if(scheduleVisit.collides(newVisit)) throw new VisitCollisionException();
        }

        patient.getVisits().add(newVisit);
        patientRepository.save(patient);

        schedule.getVisits().add(newVisit);
        scheduleRepository.save(schedule);

        visitRepository.save(newVisit);

        return new VisitDTO(newVisit);

    }

}
