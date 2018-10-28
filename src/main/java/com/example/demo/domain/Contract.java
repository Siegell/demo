package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate contractDate;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Double expectedTotalCost;
    private Double calculatedTotalCost = 0D;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Stage> stages;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;

    @JsonGetter("contractor")
    public String getContractorName(){
        return contractor == null ? "null" : contractor.getName();
    }

    public void recalculateCost() {
        double s = 0;
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
