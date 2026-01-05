package com.example.nfz.repository;

import com.example.nfz.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository  extends JpaRepository<Office, Integer> {

}
