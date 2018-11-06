package com.example.demo.controllers;

import com.example.demo.domain.Contractor;
import com.example.demo.repositories.ContractorsRepository;
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
        Map<String, Object> model = new HashMap<>();
        model.put("name", (contractor.getName() != null ? contractor.getName() : "name"));
        model.put("address", (contractor.getAddress() != null ? contractor.getAddress() : "address"));
        model.put("phone", (contractor.getPhone() != null ? contractor.getPhone() : "phone"));
        model.put("from", from);
        return new ModelAndView("contractorEdit", model);
    }

    @PostMapping("{contractorID}/edit/{from}")
    public ModelAndView save(@PathVariable Long contractorID, @PathVariable String from, @RequestParam Map<String, String> map) {
        Contractor contractor = contractorsRepository.findById(contractorID).get();
        String name = map.getOrDefault("name", "name");
        if (!Objects.equals(name, ""))
            contractor.setName(name);
        String phone = map.getOrDefault("phone", "phone");
        if (!Objects.equals(phone, ""))
            contractor.setPhone(phone);
        String address = map.getOrDefault("address", "address");
        if (!Objects.equals(address, ""))
            contractor.setAddress(address);
        if (contractorsValidator.validate(contractor, false)) {
            contractorsRepository.save(contractor);
        } else {
            Map<String, Object> model = new HashMap<>();
            model.put("name", (contractor.getName() != null ? contractor.getName() : "name"));
            model.put("address", (contractor.getAddress() != null ? contractor.getAddress() : "address"));
            model.put("phone", (contractor.getPhone() != null ? contractor.getPhone() : "phone"));
            model.put("error", "Contractor with this name already exists");
            model.put("from", from);
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
        Map<String, Object> model = new HashMap<>();
        model.put("name", "name");
        model.put("address", "address");
        model.put("phone", "phone");
        model.put("from", from);
        return new ModelAndView("contractorsAdd", model);
    }

    @PostMapping("/add/{from}")
    public ModelAndView addSaving(@PathVariable String from, @RequestParam Map<String, String> map) {
        Contractor contractor = new Contractor();
        String name = map.getOrDefault("name", "name");
        if (!Objects.equals(name, ""))
            contractor.setName(name);
        String phone = map.getOrDefault("phone", "phone");
        if (!Objects.equals(phone, ""))
            contractor.setPhone(phone);
        String address = map.getOrDefault("address", "address");
        if (!Objects.equals(address, ""))
            contractor.setAddress(address);
        if (contractorsValidator.validate(contractor, true)) {
            contractorsRepository.save(contractor);
        } else {
            Map<String, Object> model = new HashMap<>();
            model.put("name", "name");
            model.put("address", "address");
            model.put("phone", "phone");
            model.put("from", from);
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
