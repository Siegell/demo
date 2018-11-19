package com.example.demo.repositories;

import com.example.demo.domain.Contract;
import com.example.demo.domain.Contractor;
import com.example.demo.domain.QContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContractsRepository extends JpaRepository<Contract, Long>, QuerydslPredicateExecutor<Contract>, QuerydslBinderCustomizer<QContract> {
    Set<Contract> findContractByContractor(Contractor contractor);

    @Override
    default void customize(QuerydslBindings bindings, QContract root) {}
}