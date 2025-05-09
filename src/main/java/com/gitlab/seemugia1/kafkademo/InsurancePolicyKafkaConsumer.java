package com.gitlab.seemugia1.kafkademo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;

@ApplicationScoped
public class InsurancePolicyKafkaConsumer {

    private static final Logger LOGGER = Logger.getLogger(InsurancePolicyKafkaConsumer.class);

    @Inject
    InsurancePolicyRepository repository;
    private final ObjectMapper objectMapper;

    public InsurancePolicyKafkaConsumer() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Incoming("policy-consumer")
    @Transactional
    public void consume(String message) {
        LOGGER.infof("Consuming Kafka message: %s", message);

        try {
            InsurancePolicyEvent event = objectMapper.readValue(message, InsurancePolicyEvent.class);
            switch (event.getAction()) {
                case "created":
                    InsurancePolicy policy = event.getPolicy();
                    policy.setId(null);
                    persistPolicy(policy);
                    break;
                case "updated":
                    updatePolicy(event.getPolicy());
                    break;
                case "deleted":
                    deletePolicy(event.getPolicy());
                    break;
                default:
                    LOGGER.warnf("Unknown event action: %s", event.getAction());
            }
        } catch (Exception e) {
            LOGGER.error("Error processing Kafka message", e);
        }
    }

    @Transactional
    void persistPolicy(InsurancePolicy policy) {
        LOGGER.infof("Persisting new policy: %s", policy);
        repository.persist(policy);
    }

    @Transactional
    void updatePolicy(InsurancePolicy policy) {
        LOGGER.infof("Updating policy: %s", policy);
        InsurancePolicy existing = repository.findById(policy.getId());
        if (existing == null) {
            LOGGER.warnf("Policy with ID %d not found for update", policy.getId());
            return;
        }
        repository.getEntityManager().merge(policy);
    }

    @Transactional
    void deletePolicy(InsurancePolicy policy) {
        LOGGER.infof("Deleting policy: %s", policy);
        InsurancePolicy existing = repository.findById(policy.getId());
        if (existing != null) {
            repository.delete(existing);
        } else {
            LOGGER.warnf("Policy with ID %d not found for deletion", policy.getId());
        }
    }
}
