package com.example.demo;

import com.example.demo.model.Contract;
import com.example.demo.model.ContractsRepository;
import com.example.demo.model.Stage;
import com.example.demo.model.StagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
        Set<Stage> stages = new LinkedHashSet<Stage>();
        stages.add(stage);
        contract.setStages(stages);
        contractsRepository.save(contract);


        return new ModelAndView("index");
    }
}
