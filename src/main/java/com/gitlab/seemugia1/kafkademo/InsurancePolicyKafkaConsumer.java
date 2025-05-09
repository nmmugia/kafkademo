package com.gitlab.seemugia1.kafkademo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class InsurancePolicyKafkaConsumer {

    private static final Logger LOGGER = Logger.getLogger(InsurancePolicyKafkaConsumer.class);

    @Inject
    InsurancePolicyRepository repository;

    @Incoming("policy-consumer")
    @Transactional
    public void consume(InsurancePolicyEvent event) {
        LOGGER.infof("Consuming Kafka message: %s", event);

        try {
            switch (event.getAction()) {
                case "created":
                    persistPolicy(event.getPolicy());
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
