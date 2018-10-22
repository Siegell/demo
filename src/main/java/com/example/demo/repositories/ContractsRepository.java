package com.example.demo.repositories;

import com.example.demo.domain.Contract;
import org.springframework.data.repository.CrudRepository;

public interface ContractsRepository extends CrudRepository<Contract, Long>{
}