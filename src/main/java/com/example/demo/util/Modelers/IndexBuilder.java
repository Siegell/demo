package com.example.demo.util.Modelers;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Contractor;
import com.example.demo.repositories.ContractorsRepository;
import com.example.demo.repositories.ContractsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class IndexBuilder {
    @Autowired
    private ContractsRepository contractsRepository;
    @Autowired
    private ContractorsRepository contractorsRepository;

    public Map<String, Object> buildModel() {
        Map<String, Object> model = new HashMap<>();
        model.put("contractDate", LocalDate.now());
        model.put("beginDate", LocalDate.now());
        model.put("endDate", LocalDate.now());
        model.put("expectedTotalCost", 0);
        return model;
    }

    public Map<String, Object> buildModel(Contract contract) {
        Map<String, Object> model = new HashMap<>();
        model.put("contractDate", (contract.getContractDate() != null ? contract.getContractDate() : LocalDate.now()));
        model.put("beginDate", (contract.getBeginDate() != null ? contract.getBeginDate() : LocalDate.now()));
        model.put("endDate", (contract.getEndDate() != null ? contract.getEndDate() : LocalDate.now()));
        model.put("expectedTotalCost", (contract.getExpectedTotalCost() != null ? contract.getExpectedTotalCost() : 0));
        List<Contractor> contractors = contractorsRepository.findAll();
        List<String> names = new LinkedList<>();
        for (Contractor contractor : contractors) {
            names.add(contractor.getName());
        }
        model.put("contractorNames", names);
        return model;
    }

    public Contract buildContract(LocalDate beginDate, LocalDate endDate, LocalDate contractDate, Double totalCost, String contractorName) {
        Contract contract = new Contract();
        contract.setBeginDate(beginDate);
        contract.setEndDate(endDate);
        contract.setContractDate(contractDate);
        contract.setExpectedTotalCost(totalCost);
        Contractor contractor = contractorsRepository.findByName(contractorName);
        contract.setContractor(contractor);
        return contract;
    }

    public Contract buildContract(Long contractID, LocalDate beginDate, LocalDate endDate, LocalDate contractDate, Double totalCost, String contractorName) {
        Contract contract = contractsRepository.findById(contractID).get();
        contract.setBeginDate(beginDate);
        contract.setEndDate(endDate);
        contract.setContractDate(contractDate);
        contract.setExpectedTotalCost(totalCost);
        Contractor contractor = contractorsRepository.findByName(contractorName);
        contract.setContractor(contractor);
        contract.recalculateCost();
        return contract;
    }
}
