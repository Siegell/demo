package com.example.demo.repositories;

import com.example.demo.domain.Contractor;
import com.example.demo.domain.QContractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorsRepository extends JpaRepository<Contractor, Long>, QuerydslPredicateExecutor<Contractor>, QuerydslBinderCustomizer<QContractor> {
    Contractor findByName(String name);

    @Override
    default void customize(QuerydslBindings bindings, QContractor root) {}
}
