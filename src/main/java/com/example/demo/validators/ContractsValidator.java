package com.example.demo.validators;

import com.example.demo.domain.Contract;
import org.springframework.stereotype.Service;

@Service
public class ContractsValidator {

    public boolean validate(Contract contract){
        return simpleDateValidation(contract);
    }

    private boolean simpleDateValidation(Contract contract){
        return contract.getBeginDate().isBefore(contract.getEndDate()) || contract.getBeginDate().isEqual(contract.getEndDate());
    }
}
