package com.gitlab.seemugia1.kafkademo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaConsumerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumerService.class);

    @Inject
    InsurancePolicyRepository repository;

    @Incoming("policy-consumer")
    public void consume(InsurancePolicy policy) {
        LOGGER.info("Consuming policy: " + policy);
        repository.persist(policy);
    }
}
