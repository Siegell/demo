package com.example.demo.validators;

import com.example.demo.domain.Contractor;
import com.example.demo.repositories.ContractorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;


@Service
public class ContractorsValidator {
    @Autowired
    ContractorsRepository contractorsRepository;

    public boolean validate(Contractor contractor, boolean add){
        return uniqueNameValidation(contractor, add);
    }

    private boolean uniqueNameValidation(Contractor contractor, boolean add){
        try {
            if(contractorsRepository.findByName(contractor.getName()) != null && add){
                return false;
            }
        } catch(Exception e){
            return false;
        }
        return true;
    }
}
