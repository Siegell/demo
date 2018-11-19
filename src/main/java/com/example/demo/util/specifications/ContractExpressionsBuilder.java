package com.example.demo.util.specifications;

import com.example.demo.domain.QContract;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

public class ContractExpressionsBuilder implements ExpressionsBuilder {
    public BooleanExpression parsePredicate(Predicate predicate){
        QContract contract = QContract.contract;
        switch (predicate.getKey()){
            case "contractor":{
                return contract.contractor.name.like("%"+predicate.getValue()+"%");
            }
            case "beginDate":{
                switch (predicate.getOperation()){
                    case ":":{
                        return contract.beginDate.eq(LocalDate.parse(predicate.getValue()));
                    }
                    case ">":{
                        return contract.beginDate.after(LocalDate.parse(predicate.getValue()));
                    }
                    case "<":{
                        return contract.beginDate.before(LocalDate.parse(predicate.getValue()));
                    }
                }
                break;
            }
            case "endDate":{
                switch (predicate.getOperation()){
                    case ":":{
                        return contract.endDate.eq(LocalDate.parse(predicate.getValue()));
                    }
                    case ">":{
                        return contract.endDate.after(LocalDate.parse(predicate.getValue()));
                    }
                    case "<":{
                        return contract.endDate.before(LocalDate.parse(predicate.getValue()));
                    }
                }
                break;
            }
            case "contractDate":{
                switch (predicate.getOperation()){
                    case ":":{
                        return contract.contractDate.eq(LocalDate.parse(predicate.getValue()));
                    }
                    case ">":{
                        return contract.contractDate.after(LocalDate.parse(predicate.getValue()));
                    }
                    case "<":{
                        return contract.contractDate.before(LocalDate.parse(predicate.getValue()));
                    }
                }
                break;
            }
        }
        return null;
    }
}
