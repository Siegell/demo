package com.example.demo;

import com.example.demo.model.ContractsRepository;
import com.example.demo.model.Stage;
import com.example.demo.model.StagesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

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
    public ModelAndView addform(@PathVariable long contractID) {
        Stage stage = new Stage();
        stage.setContract(contractsRepository.findById(contractID).get());
        stagesRepository.save(stage);
        StringBuilder path = new StringBuilder("redirect:/contract/").append(contractID).append("/stages/").append(stage.getId()).append("/edit");
        return new ModelAndView(path.toString());
    }

    @GetMapping("/{contractID}/stages/{stageID}/edit")
    public ModelAndView editform(@PathVariable long contractID, @PathVariable long stageID) {
        return new ModelAndView("stages");
    }

}
