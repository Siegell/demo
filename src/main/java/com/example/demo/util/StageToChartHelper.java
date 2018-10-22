package com.example.demo.util;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Stage;
import com.example.demo.repositories.StagesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StageToChartHelper {
    private final StagesRepository stagesRepository;

    public StageToChartHelper(StagesRepository stagesRepository) {
        this.stagesRepository = stagesRepository;
    }

    public List<String> getStringsChart(Contract contract) {
        Set<Stage> stages = stagesRepository.findStagesByContract(contract);
        List<String> strings = new ArrayList<>();
        for (Stage stage : stages) {
            strings.add("['" + stage.getName() + "', new Date(" + stage.getBeginDate() + "), new Date(" + stage.getEndDate() + ")],");
        }
        return strings;
    }
}
