package com.example.demo;

import com.example.demo.model.Stage;
import com.example.demo.model.StagesRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StageValidator {
    final private StagesRepository stagesRepository;

    public StageValidator(StagesRepository stagesRepository) {
        this.stagesRepository = stagesRepository;
    }

    public boolean validate(Stage stage) {
        Set<Stage> stages = stagesRepository.findStagesByContract(stage.getContract());
        return simpleDateValidation(stage) && dateValidate(stage, stages);
    }

    private boolean simpleDateValidation(Stage stage) {
        return stage.getBeginDate().isBefore(stage.getEndDate()) || stage.getBeginDate().isEqual(stage.getEndDate());
    }

    private boolean dateValidate(Stage stage, Set<Stage> stages) {
        for (Stage stagei : stages) {
            if ((stage.getBeginDate().isAfter(stagei.getBeginDate()) && stage.getBeginDate().isBefore(stagei.getEndDate())) ||
                    (stage.getEndDate().isAfter(stagei.getBeginDate()) && stage.getEndDate().isBefore(stagei.getEndDate()))) {
                return false;
            }
        }
        return true;
    }
}
