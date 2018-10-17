package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contractor;
    private LocalDate contractDate;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Long expectedTotalCost;
    private Long calculatedTotalCost = 0L;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Stage> stages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getExpectedTotalCost() {
        return expectedTotalCost;
    }

    public void setExpectedTotalCost(Long expectedTotalCost) {
        this.expectedTotalCost = expectedTotalCost;
    }

    public Set<Stage> getStages() {
        return stages;
    }

    public void setStages(Set<Stage> stages) {
        this.stages = stages;
    }

    public Long getCalculatedTotalCost() {
        return calculatedTotalCost;
    }

    public void recalculateCost() {
        long s = 0;
        for (Stage stage : stages) {
            s += stage.getCost();
        }
        calculatedTotalCost = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return Objects.equals(id, contract.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
