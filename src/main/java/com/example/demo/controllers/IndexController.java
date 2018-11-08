package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.repositories.ContractorsRepository;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import com.example.demo.util.ContractsExporter;
import com.example.demo.util.builders.IndexBuilder;
import com.example.demo.validators.ContractsValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IndexBuilder indexBuilder;

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
        Map<String, Object> model = indexBuilder.buildModel(contract);
        return new ModelAndView("edit", model);
    }

    @PostMapping("/{contractID}/edit")
    public ModelAndView save(@PathVariable long contractID, @ModelAttribute(name = "beginDate") String beginDate, @ModelAttribute(name = "contractorName") String contractorName, @ModelAttribute(name = "expectedTotalCost") String totalCost, @ModelAttribute(name = "endDate") String endDate, @ModelAttribute(name = "contractDate") String contractDate) {
        Contract contract = indexBuilder.buildContract(contractID, LocalDate.parse(beginDate), LocalDate.parse(endDate), LocalDate.parse(contractDate), Double.parseDouble(totalCost), contractorName);
        if (contractsValidator.validate(contract)) {
            contractsRepository.save(contract);
        } else {
            Map<String, Object> model = indexBuilder.buildModel(contract);
            return new ModelAndView("/" + contractID + "/edit", model);
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/add")
    public ModelAndView add() {
        Map<String, Object> model = indexBuilder.buildModel();
        return new ModelAndView("add", model);
    }

    @PostMapping("/add")
    public ModelAndView addSaving(@ModelAttribute(name = "beginDate") String beginDate, @ModelAttribute(name = "contractorName") String contractorName, @ModelAttribute(name = "expectedTotalCost") String totalCost, @ModelAttribute(name = "endDate") String endDate, @ModelAttribute(name = "contractDate") String contractDate) {
        Contract contract = indexBuilder.buildContract(LocalDate.parse(beginDate), LocalDate.parse(endDate), LocalDate.parse(contractDate), Double.parseDouble(totalCost), contractorName);
        if (contractsValidator.validate(contract)) {
            contractsRepository.save(contract);
        } else {
            Map<String, Object> model = indexBuilder.buildModel(contract);
            return new ModelAndView("/add", model);
        }
        return new ModelAndView("redirect:/");
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

    @GetMapping("/export")
    public String export(@RequestParam(name = "export", required = false) List<Long> contractIDs, @RequestParam(name = "exportType") String exportType, HttpServletResponse response) {
        if (contractIDs != null) {
            List<Contract> contracts = contractsRepository.findAllById(contractIDs);
            File file = contractsExporter.createFile(contracts, exportType);
            try {
                InputStream in = new FileInputStream(file);
                if (Objects.equals(exportType, "PDF")) {
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
        } else return "redirect:/";
    }
}
