package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import com.example.demo.validators.ContractsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class IndexController {

    @Autowired
    private ContractsRepository contractsRepository;
    @Autowired
    private StagesRepository stagesRepository;
    @Autowired
    private ContractsValidator contractsValidator;

    public IndexController(ContractsRepository contractsRepository, StagesRepository stagesRepository, ContractsValidator contractsValidator) {
        this.contractsRepository = contractsRepository;
        this.stagesRepository = stagesRepository;
        this.contractsValidator = contractsValidator;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/{contractID}/edit")
    public ModelAndView editform(@PathVariable long contractID) {
        Contract contract = contractsRepository.findById(contractID).get();
        Map<String, Object> model = new HashMap<>();
        model.put("contractor", (contract.getContractor() != null ? contract.getContractor() : "contractor"));
        model.put("contractDate", (contract.getContractDate() != null ? contract.getContractDate() : LocalDate.now()));
        model.put("beginDate", (contract.getBeginDate() != null ? contract.getBeginDate() : LocalDate.now()));
        model.put("endDate", (contract.getEndDate() != null ? contract.getEndDate() : LocalDate.now()));
        model.put("expectedTotalCost", (contract.getExpectedTotalCost() != null ? contract.getExpectedTotalCost() : 0));
        return new ModelAndView("edit", model);
    }

    @PostMapping("/{contractID}/edit")
    public ModelAndView save(@PathVariable long contractID, @RequestParam Map<String, String> map) {
        Contract contract = contractsRepository.findById(contractID).get();
        contract.setContractor(map.getOrDefault("contractor", null));
        String beginDateStr = map.getOrDefault("beginDate", null);
        if (!Objects.equals(beginDateStr, ""))
            contract.setBeginDate(LocalDate.parse(beginDateStr));
        String endDateStr = map.getOrDefault("endDate", null);
        if (!Objects.equals(endDateStr, ""))
            contract.setEndDate(LocalDate.parse(endDateStr));
        String contractDateStr = map.getOrDefault("contractDate", null);
        if (!Objects.equals(contractDateStr, ""))
            contract.setContractDate(LocalDate.parse(contractDateStr));
        String totalCostStr = map.getOrDefault("expectedTotalCost", "0");
        if (!Objects.equals(totalCostStr, ""))
            contract.setExpectedTotalCost(Double.parseDouble(totalCostStr));
        contract.recalculateCost();

        if (contractsValidator.validate(contract)) {
            contractsRepository.save(contract);
        } else {
            Map<String, Object> model = new HashMap<>();
            model.put("contractor", (contract.getContractor() != null ? contract.getContractor() : "contractor"));
            model.put("contractDate", (contract.getContractDate() != null ? contract.getContractDate() : LocalDate.now()));
            model.put("beginDate", (contract.getBeginDate() != null ? contract.getBeginDate() : LocalDate.now()));
            model.put("endDate", (contract.getEndDate() != null ? contract.getEndDate() : LocalDate.now()));
            model.put("expectedTotalCost", (contract.getExpectedTotalCost() != null ? contract.getExpectedTotalCost() : 0));
            return new ModelAndView("/" + contractID + "/edit", model);
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/add")
    public ModelAndView add() {
        Contract contract = new Contract();
        contractsRepository.save(contract);
        return new ModelAndView("redirect:/" + contract.getId() + "/edit");
    }

    @RequestMapping("/{contractID}/delete")
    public ModelAndView delete(@PathVariable long contractID) {
        contractsRepository.deleteById(contractID);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/{contractID}/recalc")
    public ModelAndView recalc(@PathVariable long contractID) {
        Contract contract = contractsRepository.findById(contractID).get();
        contract.recalculateCost();
        contractsRepository.save(contract);
        return new ModelAndView("redirect:/");
    }
}
