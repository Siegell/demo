package com.example.demo.controllers;

import com.example.demo.domain.Contractor;
import com.example.demo.repositories.ContractorsRepository;
import com.example.demo.util.builders.ContractorsBuilder;
import com.example.demo.validators.ContractorsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/contractors")
public class ContractorsController {
    @Autowired
    private ContractorsRepository contractorsRepository;
    @Autowired
    private ContractorsValidator contractorsValidator;
    @Autowired
    private ContractorsBuilder contractorsBuilder;

    public ContractorsController(ContractorsRepository contractorsRepository, ContractorsValidator contractorsValidator) {
        this.contractorsRepository = contractorsRepository;
        this.contractorsValidator = contractorsValidator;
    }

    @GetMapping("")
    public ModelAndView allContractors() {
        return new ModelAndView("contractorIndex");
    }

    @GetMapping("{contractorID}/edit/{from}")
    public ModelAndView editForm(@PathVariable Long contractorID, @PathVariable String from) {
        Contractor contractor = contractorsRepository.findById(contractorID).get();
        Map<String, Object> model = contractorsBuilder.buildModel(contractor, from);
        return new ModelAndView("contractorEdit", model);
    }

    @PostMapping("{contractorID}/edit/{from}")
    public ModelAndView save(@PathVariable Long contractorID, @PathVariable String from, @ModelAttribute(name = "name") String name, @ModelAttribute(name = "phone") String phone, @ModelAttribute(name = "address") String address) {
        Contractor contractor = contractorsBuilder.buildContractor(contractorID, name, phone, address);
        if (contractorsValidator.validate(contractor, false)) {
            contractorsRepository.save(contractor);
        } else {
            Map<String, Object> model = contractorsBuilder.buildModel(contractor, from);
            model.put("error", "Contractor with this name already exists");
            return new ModelAndView("contractorEdit", model);
        }
        if (Objects.equals(from, "index")) {
            return new ModelAndView("redirect:/contractors");
        } else {
            return new ModelAndView("redirect:/" + from + "/edit");
        }
    }

    @GetMapping("/add/{from}")
    public ModelAndView add(@PathVariable String from) {
        if (from == null) {
            from = "index";
        }
        Map<String, Object> model = contractorsBuilder.buildModel(from);
        return new ModelAndView("contractorsAdd", model);
    }

    @PostMapping("/add/{from}")
    public ModelAndView addSaving(@PathVariable String from, @ModelAttribute(name = "name") String name, @ModelAttribute(name = "phone") String phone, @ModelAttribute(name = "address") String address) {
        Contractor contractor = contractorsBuilder.buildContractor(name, phone, address);
        if (contractorsValidator.validate(contractor, true)) {
            contractorsRepository.save(contractor);
        } else {
            Map<String, Object> model = contractorsBuilder.buildModel(from);
            model.put("error", "Contractor with this name already exists");
            return new ModelAndView("contractorsAdd", model);
        }
        if (Objects.equals(from, "index")) {
            return new ModelAndView("redirect:/contractors");
        } else {
            if (Objects.equals(from, "add")) {
                return new ModelAndView("redirect:/" + from);
            } else {
                return new ModelAndView("redirect:/" + from + "/edit");
            }
        }
    }

    @RequestMapping("/{contractorID}/delete")
    public ModelAndView delete(@PathVariable long contractorID) {
        contractorsRepository.deleteById(contractorID);
        return new ModelAndView("redirect:/contractors");
    }
}
