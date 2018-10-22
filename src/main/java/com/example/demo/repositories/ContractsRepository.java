package com.example.demo.repositories;

import com.example.demo.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractsRepository extends JpaRepository<Contract, Long> {
}