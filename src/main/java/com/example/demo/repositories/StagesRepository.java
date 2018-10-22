package com.example.demo.repositories;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Stage;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface StagesRepository extends CrudRepository<Stage, Long> {
    Set<Stage> findStagesByContract(Contract contract);
}
