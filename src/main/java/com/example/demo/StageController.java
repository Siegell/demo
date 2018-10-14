package com.example.demo;

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
    private final StagesRepository stagesRepository;

    public StageController(StagesRepository stagesRepository) {
        this.stagesRepository = stagesRepository;
    }

    @GetMapping("{contractID}/stages")
    public ModelAndView stages(@PathVariable long contractID){
        Map<String, String> model = new HashMap<>();
        model.put("contractID", Long.toString(contractID));


        return new ModelAndView("stages", model);
    }

}
