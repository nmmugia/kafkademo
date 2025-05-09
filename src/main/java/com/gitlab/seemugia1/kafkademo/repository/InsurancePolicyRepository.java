package com.gitlab.seemugia1.kafkademo.repository;

import com.gitlab.seemugia1.kafkademo.domain.model.InsurancePolicy;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InsurancePolicyRepository implements PanacheRepository<InsurancePolicy> {
}
