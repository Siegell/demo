package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Contractor;
import com.example.demo.domain.Stage;
import com.example.demo.repositories.ContractorsRepository;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ContractsRepository contractsRepository;
    @Autowired
    private StagesRepository stagesRepository;
    @Autowired
    private ContractorsRepository contractorsRepository;

    public ApiController(ContractsRepository contractsRepository, StagesRepository stagesRepository, ContractorsRepository contractorsRepository) {
        this.contractsRepository = contractsRepository;
        this.stagesRepository = stagesRepository;
        this.contractorsRepository = contractorsRepository;
    }

    @GetMapping("/contracts")
    public Iterable<Contract> getContracts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "order", defaultValue = "id") String order, @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        Sort sort;
        if(Objects.equals(direction, "asc")){
            sort = Sort.by(order).ascending();
        } else {
            sort = Sort.by(order).descending();
        }
        Page<Contract> contracts = contractsRepository.findAll(PageRequest.of(page, size, sort));
        return contracts;
    }

    @GetMapping("/stages/{contractID}")
    public Iterable<Stage> getContracts(@PathVariable long contractID, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "order", defaultValue = "id") String order, @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        Contract contract = contractsRepository.findById(contractID).get();
        Sort sort = Sort.by(order);
        if(direction == "asc"){
            sort.ascending();
        } else {
            sort.descending();
        }
        return stagesRepository.findStagesByContract(contract, PageRequest.of(page, size, sort)).getContent();
    }

    @GetMapping("/contractors")
    public Iterable<Contractor> getContractors(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "order", defaultValue = "id") String order, @RequestParam(name = "direction", defaultValue = "asc") String direction){
        Sort sort = Sort.by(order);
        if(direction == "asc"){
            sort.ascending();
        } else {
            sort.descending();
        }
        Page<Contractor> contractors = contractorsRepository.findAll(PageRequest.of(page, size, sort));
        return contractors.getContent();
    }
}
