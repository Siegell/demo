package com.example.demo.repositories;

import com.example.demo.domain.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorsRepository extends JpaRepository<Contractor, Long> {
    Contractor findByName(String name);
}
