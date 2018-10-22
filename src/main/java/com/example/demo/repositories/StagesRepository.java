package com.example.demo.repositories;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface StagesRepository extends JpaRepository<Stage, Long> {
    Set<Stage> findStagesByContract(Contract contract);
}
