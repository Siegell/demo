package com.example.demo;

import com.example.demo.model.ContractsRepository;
import com.example.demo.model.Stage;
import com.example.demo.model.StagesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/contract")
public class StageController {
    private final ContractsRepository contractsRepository;
    private final StagesRepository stagesRepository;

    public StageController(ContractsRepository contractsRepository, StagesRepository stagesRepository) {
        this.contractsRepository = contractsRepository;
        this.stagesRepository = stagesRepository;
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
        model.put("stageName", (stage.getName() != null ? stage.getName() : ""));
        model.put("beginDate", (stage.getBeginDate() != null ? stage.getBeginDate() : ""));
        model.put("endDate", (stage.getEndDate() != null ? stage.getEndDate() : ""));
        model.put("cost", (stage.getCost() != null ? stage.getCost() : ""));
        model.put("paymentDate", (stage.getPaymentDate() != null ? stage.getPaymentDate() : ""));
        return new ModelAndView("stagesEdit", model);
    }

    @PostMapping("/{contractID}/stages/{stageID}/edit")
    public ModelAndView save(@PathVariable long contractID, @PathVariable long stageID, @RequestParam Map<String, String> map) {
        Stage stage = stagesRepository.findById(stageID).get();
        stage.setName(map.getOrDefault("name", null));
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
            stage.setCost(Long.parseLong(costStr));
        stagesRepository.save(stage);
        return new ModelAndView("redirect:/contract/" + contractID + "/stages/");
    }

    @RequestMapping("/{contractID}/stages/{stageID}/delete")
    public ModelAndView delete(@PathVariable long contractID, @PathVariable long stageID){
        stagesRepository.deleteById(stageID);
        return new ModelAndView("redirect:/contract/" + contractID + "/stages/");
    }

}
