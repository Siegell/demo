package com.example.demo;

import com.example.demo.model.Contract;
import com.example.demo.model.ContractsRepository;
import com.example.demo.model.Stage;
import com.example.demo.model.StagesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;

@Controller
public class IndexController {

    private final ContractsRepository contractsRepository;
    private final StagesRepository stagesRepository;

    public IndexController(ContractsRepository contractsRepository, StagesRepository stagesRepository) {
        this.contractsRepository = contractsRepository;
        this.stagesRepository = stagesRepository;
    }

    @GetMapping("/")
    public ModelAndView index() {
        Contract contract = new Contract();
        contract.setContractDate(LocalDate.now());
        contract.setContractor("Danicheleos");
        contractsRepository.save(contract);
        Stage stage = new Stage();
        stage.setContract(contract);
        stage.setBeginDate(LocalDate.now());
        stage.setName("test");
        stagesRepository.save(stage);
        Set<Stage> stages = new LinkedHashSet<>();
        stages.add(stage);
        contract.setStages(stages);
        contractsRepository.save(contract);
        return new ModelAndView("index");
    }

    @GetMapping("/{contractID}/edit")
    public ModelAndView editform(@PathVariable long contractID){
        Contract contract = contractsRepository.findById(contractID).get();
        Map<String, Object> model = new HashMap<>();
        model.put("contractor", (contract.getContractor() != null ? contract.getContractor() : ""));
        model.put("contractDate", (contract.getContractDate() != null ? contract.getContractDate() : ""));
        model.put("beginDate", (contract.getBeginDate() != null ? contract.getBeginDate() : ""));
        model.put("endDate", (contract.getEndDate() != null ? contract.getEndDate() : ""));
        model.put("totalCost", (contract.getTotalCost() != null ? contract.getTotalCost() : ""));
        return new ModelAndView("edit", model);
    }

    @PostMapping("/{contractID}/edit")
    public ModelAndView save(@PathVariable long contractID,  @RequestParam Map<String, String> map){
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
        String totalCostStr = map.getOrDefault("totalCost", "0");
        if (!Objects.equals(totalCostStr, ""))
            contract.setTotalCost(Long.parseLong(totalCostStr));
        contractsRepository.save(contract);
        return new ModelAndView("redirect:/");
    }
}
