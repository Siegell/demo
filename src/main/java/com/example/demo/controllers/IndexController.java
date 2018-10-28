package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Contractor;
import com.example.demo.repositories.ContractorsRepository;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import com.example.demo.util.ContractsExporter;
import com.example.demo.validators.ContractsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    private ContractsRepository contractsRepository;
    @Autowired
    private StagesRepository stagesRepository;
    @Autowired
    private ContractorsRepository contractorsRepository;
    @Autowired
    private ContractsValidator contractsValidator;
    @Autowired
    private ContractsExporter contractsExporter;

    public IndexController(ContractsRepository contractsRepository, StagesRepository stagesRepository, ContractsValidator contractsValidator, ContractsExporter contractsExporter) {
        this.contractsRepository = contractsRepository;
        this.stagesRepository = stagesRepository;
        this.contractsValidator = contractsValidator;
        this.contractsExporter = contractsExporter;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/{contractID}/edit")
    public ModelAndView editform(@PathVariable long contractID) {
        Contract contract = contractsRepository.findById(contractID).get();
        Map<String, Object> model = new HashMap<>();
        List<Contractor> contractors = contractorsRepository.findAll();
        List<String> names = new LinkedList<>();
        for (Contractor contractor : contractors) {
            names.add(contractor.getName());
        }
        model.put("contractorNames", names);
        model.put("contractDate", (contract.getContractDate() != null ? contract.getContractDate() : LocalDate.now()));
        model.put("beginDate", (contract.getBeginDate() != null ? contract.getBeginDate() : LocalDate.now()));
        model.put("endDate", (contract.getEndDate() != null ? contract.getEndDate() : LocalDate.now()));
        model.put("expectedTotalCost", (contract.getExpectedTotalCost() != null ? contract.getExpectedTotalCost() : 0));
        return new ModelAndView("edit", model);
    }

    @PostMapping("/{contractID}/edit")
    public ModelAndView save(@PathVariable long contractID, @RequestParam Map<String, String> map) {
        Contract contract = contractsRepository.findById(contractID).get();
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

        String contractorName = map.get("contractorName");
        Contractor contractor = contractorsRepository.findByName(contractorName);
        contract.setContractor(contractor);

        if (contractsValidator.validate(contract)) {
            contractsRepository.save(contract);
        } else {
            Map<String, Object> model = new HashMap<>();
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

    @GetMapping(value = "/export")
    public String export(@RequestParam(name = "export", required = false) List<Long> contractIDs, @RequestParam(name = "exportType") String exportType, HttpServletResponse response) {
        if(contractIDs != null) {
            List<Contract> contracts = contractsRepository.findAllById(contractIDs);
            File file = contractsExporter.createFile(contracts, exportType);
            try {
                InputStream in = new FileInputStream(file);
                if(Objects.equals(exportType, "PDF")){
                    response.setContentType("application/pdf");
                } else {
                    response.setContentType("text/csv");
                }
                response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
                response.setHeader("Content-Length", String.valueOf(file.length()));
                FileCopyUtils.copy(in, response.getOutputStream());
            } catch (IOException e) {
            }
            return null;
        }
        else return "redirect:/";
    }
}
