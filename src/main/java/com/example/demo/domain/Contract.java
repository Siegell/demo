package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class Contract {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String contractor;
    @Getter
    @Setter
    private LocalDate contractDate;
    @Getter
    @Setter
    private LocalDate beginDate;
    @Getter
    @Setter
    private LocalDate endDate;
    @Getter
    @Setter
    private Double expectedTotalCost;
    @Getter
    private Double calculatedTotalCost = 0D;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Stage> stages;


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
