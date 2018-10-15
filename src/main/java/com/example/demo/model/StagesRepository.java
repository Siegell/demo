package com.example.demo.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface StagesRepository extends CrudRepository<Stage, Long> {
    Set<Stage> findStagesByContract(Contract contract);
}
