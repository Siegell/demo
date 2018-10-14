package com.example.demo;

import com.example.demo.model.Contract;
import com.example.demo.model.ContractsRepository;
import com.example.demo.model.Stage;
import com.example.demo.model.StagesRepository;
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
        return stagesRepository.findStagesByContractId(contractID);
    }
}
