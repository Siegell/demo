package com.example.demo.util.specifications;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

public interface ExpressionsBuilder {
    List<Predicate> predicates = new ArrayList<>();
    default void addPredicate(String key, String operation, String value, String predicateOperation) {
        predicates.add(new Predicate(key, operation, value, predicateOperation));
    }

    default BooleanExpression build() {
        if (predicates.size() == 0) {
            return null;
        }

        Predicate lastPredicate = predicates.get(predicates.size()-1);
        BooleanExpression booleanExpression = parsePredicate(lastPredicate);
        for (int i = predicates.size() - 2; i >= 0; i--) {
            Predicate predicate = predicates.get(i);
            BooleanExpression tempBooleanExpression = parsePredicate(predicate);
            switch(predicate.getPredicateOperation()){
                case ",": {
                    tempBooleanExpression = tempBooleanExpression.and(booleanExpression);
                    break;
                }
                case "*": {
                    tempBooleanExpression = tempBooleanExpression.or(booleanExpression);
                    break;
                }
            }
            booleanExpression = tempBooleanExpression;
        }
        return booleanExpression;
    }

    BooleanExpression parsePredicate(Predicate predicate);
}
