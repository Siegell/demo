package com.example.demo.repositories;

import com.example.demo.domain.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorsRepository extends JpaRepository<Contractor, Long> {
}
