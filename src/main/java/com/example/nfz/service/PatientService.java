package com.example.nfz.service;

import com.example.nfz.model.Office;
import com.example.nfz.model.Patient;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.repository.PatientRepository;
import com.example.nfz.util.dto.DetailedPatientDTO;
import com.example.nfz.util.dto.FormPatientDTO;
import com.example.nfz.util.dto.PatientDTO;
import com.example.nfz.util.exceptions.PatientNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
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
     */
    public void  deletePatientById(int id) throws PatientNotFoundException {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }

    /**
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

