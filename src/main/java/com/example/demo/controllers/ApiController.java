package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Stage;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final ContractsRepository contractsRepository;
    private final StagesRepository stagesRepository;

    public ApiController(ContractsRepository contractsRepository, StagesRepository stagesRepository) {
        this.contractsRepository = contractsRepository;
        this.stagesRepository = stagesRepository;
    }

    @GetMapping("/contracts")
    public Iterable<Contract> getContracts(){
        return contractsRepository.findAll();
    }

    @GetMapping("/stages/{contractID}")
    public Iterable<Stage> getContracts(@PathVariable long contractID){
        Contract contract = contractsRepository.findById(contractID).get();
        return stagesRepository.findStagesByContract(contract);
    }
}
