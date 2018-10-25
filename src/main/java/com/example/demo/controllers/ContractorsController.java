package com.example.demo.controllers;

import com.example.demo.repositories.ContractorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contractors")
public class ContractorsController {
    @Autowired
    private ContractorsRepository contractorsRepository;

    public ContractorsController(ContractorsRepository contractorsRepository) {
        this.contractorsRepository = contractorsRepository;
    }

    @GetMapping
    public ModelAndView allContractors(){
        return new ModelAndView("contractorIndex");
    }
}
