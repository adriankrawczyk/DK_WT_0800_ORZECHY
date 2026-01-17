package com.example.nfz.repository;

import com.example.nfz.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
}
