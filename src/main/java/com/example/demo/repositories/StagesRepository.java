package com.example.demo.repositories;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StagesRepository extends JpaRepository<Stage, Long> {
    Page<Stage> findStagesByContract(Contract contract, Pageable pageable);
    Set<Stage> findStagesByContract(Contract contract);
}
