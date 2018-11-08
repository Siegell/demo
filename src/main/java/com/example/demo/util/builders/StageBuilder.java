package com.example.demo.util.builders;

import com.example.demo.domain.Stage;
import com.example.demo.repositories.ContractsRepository;
import com.example.demo.repositories.StagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class StageBuilder {
    @Autowired
    private ContractsRepository contractsRepository;
    @Autowired
    private StagesRepository stagesRepository;

    public Map<String, Object> buildModel(Stage stage) {
        Map<String, Object> model = new HashMap<>();
        model.put("stageName", (stage.getName() != null ? stage.getName() : "name"));
        model.put("beginDate", (stage.getBeginDate() != null ? stage.getBeginDate() : LocalDate.now()));
        model.put("endDate", (stage.getEndDate() != null ? stage.getEndDate() : LocalDate.now()));
        model.put("cost", (stage.getCost() != null ? stage.getCost() : "0"));
        model.put("paymentDate", (stage.getPaymentDate() != null ? stage.getPaymentDate() : LocalDate.now()));
        return model;
    }

    public Map<String, Object> buildModel(Long contractID) {
        Map<String, Object> model = new HashMap<>();
        model.put("stageName", "name");
        model.put("beginDate", LocalDate.now());
        model.put("endDate", LocalDate.now());
        model.put("cost", "0");
        model.put("paymentDate", LocalDate.now());
        model.put("contractID", contractID);
        return model;
    }

    public Stage buildStage(Long contractID, String stageName, LocalDate beginDate, LocalDate endDate, LocalDate paymentDate, Double cost) {
        Stage stage = new Stage();
        stage.setContract(contractsRepository.findById(contractID).get());
        stage.setName(stageName);
        stage.setBeginDate(beginDate);
        stage.setEndDate(endDate);
        stage.setPaymentDate(paymentDate);
        stage.setCost(cost);
        return stage;
    }

    public Stage buildStage(Long stageID, Long contractID, String stageName, LocalDate beginDate, LocalDate endDate, LocalDate paymentDate, Double cost) {
        Stage stage = stagesRepository.findById(stageID).get();
        stage.setContract(contractsRepository.findById(contractID).get());
        stage.setName(stageName);
        stage.setBeginDate(beginDate);
        stage.setEndDate(endDate);
        stage.setPaymentDate(paymentDate);
        stage.setCost(cost);
        return stage;
    }

}
