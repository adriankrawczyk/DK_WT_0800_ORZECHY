package com.example.nfz.service;

import com.example.nfz.model.Office;
import com.example.nfz.repository.OfficeRepository;
import com.example.nfz.util.dto.DetailedOfficeDTO;
import com.example.nfz.util.dto.FormOfficeDTO;
import com.example.nfz.util.dto.OfficeDTO;
import com.example.nfz.util.exceptions.IsBeingScheduledException;
import com.example.nfz.util.exceptions.OfficeAlreadyExistsException;
import com.example.nfz.util.exceptions.OfficeNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfficeService {
    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @PostConstruct
    private void onServiceStarted(){
        System.out.println("office service starting stuff...");
    }

    /**
     * Returns the list of all offices in the database
     *
     * @return list of found {@link Office}
     */
    public List<Office>  getOffices()
    {
        return officeRepository.findAll();
    }

    /**
     * Returns the list of all offices in the database
     * and converts it to {@link OfficeDTO}
     *
     * @return list of {@link OfficeDTO}
     */
    public List<OfficeDTO> getOfficeDTOs(){
        return getOffices().stream()
                .map(OfficeDTO::new)
                .toList();
    }

    /**
     * Returns an office by its private identifier
     *
     * @param id unique office id
     * @return found {@link Office}
     * @throws OfficeNotFoundException if no doctor exists with given id
     */
    public Office getOfficeById(int id) throws OfficeNotFoundException {
        return officeRepository.findById(id).orElseThrow(OfficeNotFoundException::new);
    }

    /**
     * Returns an office by its private identifier
     * converted to {@link DetailedOfficeDTO}
     *
     * @param id unique office id
     * @return found {@link Office} mapped to {@link DetailedOfficeDTO}
     * @throws OfficeNotFoundException if no doctor exists with given id
     */
    public DetailedOfficeDTO getDetailedOfficeDtoById(int id) throws OfficeNotFoundException {
        return new DetailedOfficeDTO(getOfficeById(id));
    }

    /**
     * Deletes an office by its private identifier
     *
     * @param id unique office id
     * @throws OfficeNotFoundException if no office exists with given id
     * @throws IsBeingScheduledException if there exists a schedule with this office
     */
    @Transactional
    public void deleteOfficeById(int id) throws OfficeNotFoundException, IsBeingScheduledException {
        Office office = getOfficeById(id);
        if(!office.getSchedules().isEmpty()) throw new IsBeingScheduledException();

        officeRepository.delete(office);
    }

    /**
     * Adds a new office to the database
     *
     * @param office form for adding an office
     * @return created {@link Office}
     * @throws OfficeAlreadyExistsException if there exists an office with this room number
     */
    public Office saveOffice(FormOfficeDTO office) throws OfficeAlreadyExistsException {
        try{
        return officeRepository.save(new Office(office.roomNumber()));
        } catch (DataIntegrityViolationException ex){
            throw new OfficeAlreadyExistsException();
        }
    }

}
