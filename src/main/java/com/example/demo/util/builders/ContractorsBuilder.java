package com.example.demo.util.builders;

import com.example.demo.domain.Contractor;
import com.example.demo.repositories.ContractorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContractorsBuilder {
    @Autowired
    private ContractorsRepository contractorsRepository;

    public Map<String, Object> buildModel(Contractor contractor, String from) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", (contractor.getName() != null ? contractor.getName() : "name"));
        model.put("address", (contractor.getAddress() != null ? contractor.getAddress() : "address"));
        model.put("phone", (contractor.getPhone() != null ? contractor.getPhone() : "phone"));
        model.put("from", from);
        return model;
    }

    public Map<String, Object> buildModel(String from) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "name");
        model.put("address", "address");
        model.put("phone", "phone");
        model.put("from", from);
        return model;
    }

    public Contractor buildContractor(Long contractorID, String name, String phone, String address) {
        Contractor contractor = contractorsRepository.findById(contractorID).get();
        contractor.setName(name);
        contractor.setPhone(phone);
        contractor.setAddress(address);
        return contractor;
    }

    public Contractor buildContractor(String name, String phone, String address) {
        Contractor contractor = new Contractor();
        contractor.setName(name);
        contractor.setPhone(phone);
        contractor.setAddress(address);
        return contractor;
    }
}