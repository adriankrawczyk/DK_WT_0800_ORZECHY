package com.example.nfz.service;

import com.example.nfz.model.Specialization;
import com.example.nfz.model.Doctor;
import com.example.nfz.repository.DoctorRepository;
import com.example.nfz.util.dto.DetailedDoctorDTO;
import com.example.nfz.util.dto.DetailedOfficeDTO;
import com.example.nfz.util.dto.DoctorDTO;
import com.example.nfz.util.dto.FormDoctorDTO;
import com.example.nfz.util.exceptions.DoctorNotFoundException;
import com.example.nfz.util.exceptions.IsBeingScheduledException;
import com.example.nfz.util.exceptions.SpecializationNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for managing {@link Doctor} entities.
 *
 * <p>Provides CRUD operations and business validation logic.</p>
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;


    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PostConstruct
    private void onServiceStarted(){
        System.out.println("doctor service starting stuff...");
    }


    /**
     * Returns the list of all doctors in the database
     *
     * @return list of found {@link Doctor}
     */
    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    /**
     * Returns the list of all doctors in the database
     * and converts it to {@link DoctorDTO}
     *
     * @return list of {@link DoctorDTO}
     */
    public List<DoctorDTO> getDoctorDTOs(){
        return getDoctors().stream()
            .map(DoctorDTO::new)
            .toList();
    }

    /**
     * Returns a doctor by its private identifier
     *
     * @param id unique doctor id
     * @return found {@link Doctor}
     * @throws DoctorNotFoundException if no doctor exists with given id
     */
    public Doctor getDoctorById(int id) throws DoctorNotFoundException {
        return doctorRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
    }

    /**
     * Returns a doctor by its private identifier
     * converted to {@link DetailedOfficeDTO}
     *
     * @param id unique doctor id
     * @return found {@link Doctor} mapped to {@link DoctorDTO}
     * @throws DoctorNotFoundException if no doctor exists with given id
     */
    public DetailedDoctorDTO getDetailedDoctorDTOById(int id) throws DoctorNotFoundException {
        return new DetailedDoctorDTO(getDoctorById(id));
    }

    /**
     * Deletes a doctor by its private identifier
     *
     * @param id unique doctor id
     * @throws DoctorNotFoundException if no doctor exists with given id
     */
    public void deleteDoctorById(int id) throws DoctorNotFoundException, IsBeingScheduledException {
        Doctor doctor = getDoctorById(id);
        if(!doctor.getSchedules().isEmpty()) throw new IsBeingScheduledException();
        doctorRepository.delete(doctor);
    }

    /**
     * Adds a new doctor to the database
     *
     * @param doctor form for adding a doctor
     * @return created {@link Doctor}
     * @throws SpecializationNotFoundException if given specialization is not supported
     */
    public Doctor saveDoctor(FormDoctorDTO doctor) throws SpecializationNotFoundException {
        Specialization specialization = Specialization.getSpecialization(doctor.specialization());

        return doctorRepository.save(new Doctor(doctor.firstName(),doctor.lastName(),
                doctor.PESEL(),specialization,doctor.street(),doctor.city(),doctor.zipcode()));
    }


}
