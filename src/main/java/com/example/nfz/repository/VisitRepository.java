package com.example.nfz.repository;

import com.example.nfz.model.Visit;
import com.example.nfz.util.dto.VisitDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    List<Visit> findAllByPatient_Id(int id);

    List<Visit> findAllBySchedule_Id(int id);
}
