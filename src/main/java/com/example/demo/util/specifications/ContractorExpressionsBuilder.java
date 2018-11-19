package com.example.demo.util.specifications;

import com.example.demo.domain.QContractor;
import com.querydsl.core.types.dsl.BooleanExpression;

public class ContractorExpressionsBuilder implements ExpressionsBuilder{
    public BooleanExpression parsePredicate(Predicate predicate){
        QContractor contractor = QContractor.contractor;
        switch (predicate.getKey()){
            case "name":{
                return contractor.name.like("%" + predicate.getValue() + "%");
            }
        }
        return null;
    }
}
