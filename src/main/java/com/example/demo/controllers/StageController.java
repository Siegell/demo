package com.example.demo.controllers;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Stage;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import com.example.demo.validators.StageValidator;
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
    private final ContractsRepository contractsRepository;
    private final StagesRepository stagesRepository;
    private final StageValidator stageValidator;

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

    @RequestMapping("/{contractID}/stages/add")
    public ModelAndView add(@PathVariable long contractID) {
        Stage stage = new Stage();
        stage.setContract(contractsRepository.findById(contractID).get());
        stagesRepository.save(stage);
        return new ModelAndView("redirect:/contract/" + contractID + "/stages/" + stage.getId() + "/edit");
    }

    @GetMapping("/{contractID}/stages/{stageID}/edit")
    public ModelAndView editform(@PathVariable long contractID, @PathVariable long stageID) {
        Stage stage = stagesRepository.findById(stageID).get();
        Map<String, Object> model = new HashMap<>();
        model.put("stageName", (stage.getName() != null ? stage.getName() : "name"));
        model.put("beginDate", (stage.getBeginDate() != null ? stage.getBeginDate() : LocalDate.now()));
        model.put("endDate", (stage.getEndDate() != null ? stage.getEndDate() : LocalDate.now()));
        model.put("cost", (stage.getCost() != null ? stage.getCost() : "0"));
        model.put("paymentDate", (stage.getPaymentDate() != null ? stage.getPaymentDate() : LocalDate.now()));
        return new ModelAndView("stagesEdit", model);
    }

    @PostMapping("/{contractID}/stages/{stageID}/edit")
    public ModelAndView save(@PathVariable long contractID, @PathVariable long stageID, @RequestParam Map<String, String> map) {
        Stage stage = stagesRepository.findById(stageID).get();
        stage.setName(map.getOrDefault("stageName", null));
        String beginDateStr = map.getOrDefault("beginDate", null);
        if (!Objects.equals(beginDateStr, ""))
            stage.setBeginDate(LocalDate.parse(beginDateStr));
        String endDateStr = map.getOrDefault("endDate", null);
        if (!Objects.equals(endDateStr, ""))
            stage.setEndDate(LocalDate.parse(endDateStr));
        String paymentDateStr = map.getOrDefault("paymentDate", null);
        if (!Objects.equals(paymentDateStr, ""))
            stage.setPaymentDate(LocalDate.parse(paymentDateStr));
        String costStr = map.getOrDefault("cost", "0");
        if (!Objects.equals(costStr, ""))
            stage.setCost(Double.parseDouble(costStr));

        if (stageValidator.validate(stage)) {
            stagesRepository.save(stage);
            Contract contract = contractsRepository.findById(contractID).get();
            contract.recalculateCost();
            contractsRepository.save(contract);
        } else {
            Map<String, Object> model = new HashMap<>();
            model.put("stageName", (stage.getName() != null ? stage.getName() : "name"));
            model.put("beginDate", (stage.getBeginDate() != null ? stage.getBeginDate() : LocalDate.now()));
            model.put("endDate", (stage.getEndDate() != null ? stage.getEndDate() : LocalDate.now()));
            model.put("cost", (stage.getCost() != null ? stage.getCost() : "0"));
            model.put("paymentDate", (stage.getPaymentDate() != null ? stage.getPaymentDate() : LocalDate.now()));
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