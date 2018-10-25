package com.example.demo.repositories;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContractsRepository extends JpaRepository<Contract, Long> {
    Set<Contract> findContractByContractor(Contractor contractor);
}