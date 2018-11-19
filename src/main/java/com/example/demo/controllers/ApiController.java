package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Contractor;
import com.example.demo.domain.QContract;
import com.example.demo.domain.Stage;
import com.example.demo.repositories.ContractorsRepository;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import com.example.demo.util.specifications.ContractExpressionsBuilder;
import com.example.demo.util.specifications.ContractorExpressionsBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Iterable<Contract> getContracts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "order", defaultValue = "id") String order, @RequestParam(name = "direction", defaultValue = "asc") String direction, @RequestParam(value = "filter", required = false) String filter) {
        Sort sort;
        if (Objects.equals(direction, "asc")) {
            sort = Sort.by(order).ascending();
        } else {
            sort = Sort.by(order).descending();
        }
        Page<Contract> contracts;
        if (!Objects.equals(filter, "")) {
            ContractExpressionsBuilder builder = new ContractExpressionsBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)([\\w-]+)(,|\\*)", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(filter + ",");
            while (matcher.find()) {
                builder.addPredicate(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
            }
            BooleanExpression spec = builder.build();
            builder.clean();
            contracts = contractsRepository.findAll(spec, PageRequest.of(page, size, sort));
        } else {
            contracts = contractsRepository.findAll(PageRequest.of(page, size, sort));
        }
        return contracts;
    }

    @GetMapping("/stages/{contractID}")
    public Iterable<Stage> getContracts(@PathVariable long contractID, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "order", defaultValue = "id") String order, @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        Contract contract = contractsRepository.findById(contractID).get();
        Sort sort;
        if (Objects.equals(direction, "asc")) {
            sort = Sort.by(order).ascending();
        } else {
            sort = Sort.by(order).descending();
        }
        return stagesRepository.findStagesByContract(contract, PageRequest.of(page, size, sort));
    }

    @GetMapping("/contractors")
    public Iterable<Contractor> getContractors(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "order", defaultValue = "id") String order, @RequestParam(name = "direction", defaultValue = "asc") String direction, @RequestParam(value = "filter", required = false) String filter) {
        Sort sort;
        if (Objects.equals(direction, "asc")) {
            sort = Sort.by(order).ascending();
        } else {
            sort = Sort.by(order).descending();
        }
        Page<Contractor> contractors;
        if (!Objects.equals(filter, "")) {
            ContractorExpressionsBuilder builder = new ContractorExpressionsBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)([\\w-]+)(,|\\*)", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(filter + ",");
            while (matcher.find()) {
                builder.addPredicate(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
            }
            BooleanExpression spec = builder.build();
            builder.clean();
            contractors = contractorsRepository.findAll(spec, PageRequest.of(page, size, sort));
        } else {
            contractors = contractorsRepository.findAll(PageRequest.of(page, size, sort));
        }
        return contractors;
    }

    @GetMapping("/contractors/all")
    public Iterable<Contractor> getContractorsAll(){
        return contractorsRepository.findAll();
    }
}
