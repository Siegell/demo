package com.example.demo.controllers;

import com.example.demo.domain.Contractor;
import com.example.demo.repositories.ContractorsRepository;
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

    public ContractorsController(ContractorsRepository contractorsRepository) {
        this.contractorsRepository = contractorsRepository;
    }

    @GetMapping("")
    public ModelAndView allContractors(){
        return new ModelAndView("contractorIndex");
    }

    @GetMapping("{contractorID}/edit")
    public ModelAndView editForm(@PathVariable Long contractorID){
        Contractor contractor = contractorsRepository.findById(contractorID).get();
        Map<String, Object> model = new HashMap<>();
        model.put("name", (contractor.getName() != null ? contractor.getName() : "name"));
        model.put("address", (contractor.getAddress() != null ? contractor.getAddress() : "address"));
        model.put("phone", (contractor.getPhone() != null ? contractor.getPhone() : "phone"));
        return new ModelAndView("contractorEdit", model);
    }

    @PostMapping("{contractorID}/edit")
    public ModelAndView save(@PathVariable Long contractorID, @RequestParam Map<String, String> map){
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
        contractorsRepository.save(contractor);
        return new ModelAndView("redirect:/contractors");
    }

    @RequestMapping("/add")
    public ModelAndView add(){
        Contractor contractor = new Contractor();
        contractorsRepository.save(contractor);
        return new ModelAndView("redirect:/contractors/" + contractor.getId() + "/edit");
    }

    @RequestMapping("/{contractorID}/delete")
    public ModelAndView delete(@PathVariable long contractorID) {
        contractorsRepository.deleteById(contractorID);
        return new ModelAndView("redirect:/contractors");
    }
}
