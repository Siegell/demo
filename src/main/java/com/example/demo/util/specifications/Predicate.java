package com.example.demo.util.specifications;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Predicate {
    private String key;
    private String operation;
    private String value;
    private String predicateOperation;
}
