package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Stage;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import com.example.demo.util.builders.StageBuilder;
import com.example.demo.validators.StageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Controller
@RequestMapping("/contract")
public class StageController {
    @Autowired
    private ContractsRepository contractsRepository;
    @Autowired
    private StagesRepository stagesRepository;
    @Autowired
    private StageValidator stageValidator;
    @Autowired
    private StageBuilder stageBuilder;

    public StageController(ContractsRepository contractsRepository, StagesRepository stagesRepository, StageValidator stageValidator) {
        this.contractsRepository = contractsRepository;
        this.stagesRepository = stagesRepository;
        this.stageValidator = stageValidator;
    }

    @GetMapping("/{contractID}/stages")
    public ModelAndView stages(@PathVariable long contractID) {
        Map<String, String> model = new HashMap<>();
        model.put("contractID", Long.toString(contractID));
        return new ModelAndView("stages", model);
    }

    @GetMapping("/{contractID}/stages/add")
    public ModelAndView addForm(@PathVariable long contractID) {
        Map<String, Object> model = stageBuilder.buildModel(contractID);
        return new ModelAndView("stagesAdd", model);
    }

    @PostMapping("/{contractID}/stages/add")
    public ModelAndView addSave(@PathVariable long contractID, @ModelAttribute(name = "beginDate") String beginDate, @ModelAttribute(name = "stageName") String stageName, @ModelAttribute(name = "cost") String cost, @ModelAttribute(name = "endDate") String endDate, @ModelAttribute(name = "paymentDate") String paymentDate) {
        Stage stage = stageBuilder.buildStage(contractID, stageName, LocalDate.parse(beginDate), LocalDate.parse(endDate), LocalDate.parse(paymentDate), Double.parseDouble(cost));
        if (stageValidator.validate(stage)) {
            stagesRepository.save(stage);
            Contract contract = contractsRepository.findById(contractID).get();
            contract.recalculateCost();
            contractsRepository.save(contract);
        } else {
            Map<String, Object> model = stageBuilder.buildModel(stage);
            model.put("error", "Input data is not correct");
            return new ModelAndView("stagesAdd", model);
        }
        return new ModelAndView("redirect:/contract/" + contractID + "/stages/");
    }

    @GetMapping("/{contractID}/stages/{stageID}/edit")
    public ModelAndView editform(@PathVariable long contractID, @PathVariable long stageID) {
        Stage stage = stagesRepository.findById(stageID).get();
        Map<String, Object> model = stageBuilder.buildModel(stage);
        return new ModelAndView("stagesEdit", model);
    }

    @PostMapping("/{contractID}/stages/{stageID}/edit")
    public ModelAndView save(@PathVariable long contractID, @PathVariable long stageID, @ModelAttribute(name = "beginDate") String beginDate, @ModelAttribute(name = "stageName") String stageName, @ModelAttribute(name = "cost") String cost, @ModelAttribute(name = "endDate") String endDate, @ModelAttribute(name = "paymentDate") String paymentDate) {
        Stage stage = stageBuilder.buildStage(stageID, contractID, stageName, LocalDate.parse(beginDate), LocalDate.parse(endDate), LocalDate.parse(paymentDate), Double.parseDouble(cost));
        if (stageValidator.validate(stage)) {
            stagesRepository.save(stage);
            Contract contract = contractsRepository.findById(contractID).get();
            contract.recalculateCost();
            contractsRepository.save(contract);
        } else {
            Map<String, Object> model = stageBuilder.buildModel(stage);
            model.put("error", "Input data is not correct");
            return new ModelAndView("stagesEdit", model);
        }
        return new ModelAndView("redirect:/contract/" + contractID + "/stages/");
    }

    @RequestMapping("/{contractID}/stages/{stageID}/delete")
    public ModelAndView delete(@PathVariable long contractID, @PathVariable long stageID) {
        stagesRepository.deleteById(stageID);
        return new ModelAndView("redirect:/contract/" + contractID + "/stages/");
    }

    @RequestMapping("/{contractID}/chart")
    public String chart(@PathVariable long contractID, Model model) {
        Contract contract = contractsRepository.findById(contractID).get();
        Set<Stage> stages = stagesRepository.findStagesByContract(contract);
        model.addAttribute("stages", stages);
        model.addAttribute("contractID", contractID);
        return "stagesChart";
    }
}
