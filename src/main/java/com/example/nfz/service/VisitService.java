package com.example.nfz.service;

import com.example.nfz.model.Visit;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.repository.ScheduleRepository;
import com.example.nfz.repository.VisitRepository;
import com.example.nfz.util.dto.SuggestionVisitDTO;
import com.example.nfz.util.dto.VisitDTO;
import com.example.nfz.util.exceptions.VisitNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;

    public VisitService(VisitRepository visitRepository, PatientRepository patientRepository, ScheduleRepository scheduleRepository) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.scheduleRepository = scheduleRepository;
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

    public VisitDTO getVisitDTOById(int id) throws VisitNotFoundException {
        return new VisitDTO(getVisitById(id));
    }

    public List<SuggestionVisitDTO> getAvaliableVisits(String specialization ) {
        return null;
    }

}
