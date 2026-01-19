package com.example.nfz.service;

import com.example.nfz.model.Office;
import com.example.nfz.model.Patient;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.repository.VisitRepository;
import com.example.nfz.util.dto.DetailedPatientDTO;
import com.example.nfz.util.dto.FormPatientDTO;
import com.example.nfz.util.dto.PatientDTO;
import com.example.nfz.util.exceptions.PatientNotFoundException;
import com.example.nfz.util.exceptions.VisitScheduledException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public PatientService(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    @PostConstruct
    private void onServiceStarted(){
        System.out.println("office service starting stuff...");
    }


    /**
     * Returns the list of all patients in the database
     *
     * @return list of found {@link Patient}
     */
    public List<Patient> getPatients()
    {
        return patientRepository.findAll();
    }

    /**
     * Returns the list of all patients in the database
     * and converts it to {@link PatientDTO}
     *
     * @return list of found {@link PatientDTO}
     */
    public List<PatientDTO> getPatientDTOs()
    {
        return getPatients().stream()
                .map(PatientDTO::new)
                .toList();
    }

    /**
     * Returns a patient by its private identifier
     *
     * @param id
     * @return found {@link Patient}
     * @throws PatientNotFoundException if no patient exists with given id
     */
    public Patient getPatientById(int id) throws PatientNotFoundException {
        return patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
    }

    /**
     * Returns a patient by its private identifier
     *
     * @param id
     * @return found {@link Patient} mapped to {@link DetailedPatientDTO}
     * @throws PatientNotFoundException if no patient exists with given id
     */
    public DetailedPatientDTO getDetailedPatientDTOById(int id) throws PatientNotFoundException {
        return new DetailedPatientDTO(getPatientById(id));
    }

    /**
     * Deletes a doctor by its private identifier
     *
     * @param id
     * @throws PatientNotFoundException if no patient exists with given id
     * @throws VisitScheduledException if patient has a future visit scheduled
     */
    public void  deletePatientById(int id) throws PatientNotFoundException, VisitScheduledException {
        Patient patient = getPatientById(id);
        if(patient.getVisits().stream().anyMatch(visit -> !visit.getDate().isBefore(LocalDate.now())))
            throw new VisitScheduledException();
        visitRepository.deleteAll(patient.getVisits());
        patientRepository.delete(patient);
    }

    /**
     *
     * Adds a new patient to the database
     *
     * @param patient form for adding a patient
     * @return created {@link Patient}
     */
    public DetailedPatientDTO savePatient(FormPatientDTO patient){
        return new DetailedPatientDTO( patientRepository.save(new Patient(patient.firstName(), patient.lastName(),
                patient.PESEL(), patient.street(), patient.city(), patient.zipCode())));
    }

}

