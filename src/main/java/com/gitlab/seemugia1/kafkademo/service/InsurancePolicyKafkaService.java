package com.gitlab.seemugia1.kafkademo.service;

import com.gitlab.seemugia1.kafkademo.domain.model.InsurancePolicy;
import com.gitlab.seemugia1.kafkademo.domain.event.InsurancePolicyEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ApplicationScoped
public class InsurancePolicyKafkaService {

    private static final Logger LOGGER = Logger.getLogger(InsurancePolicyKafkaService.class);

    @Inject
    @Channel("policy-events-out")
    Emitter<String> emitter;

    private final ObjectMapper objectMapper;

    public InsurancePolicyKafkaService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void sendPolicyEvent(String action, InsurancePolicy policy) {
        InsurancePolicyEvent event = new InsurancePolicyEvent(action, policy);
        LOGGER.infof("Sending Kafka event: %s", event);
        try {
            String json = objectMapper.writeValueAsString(event);
            emitter.send(json);
        } catch (Exception e) {
            LOGGER.errorf("Failed to publish Kafka message: %s", e.getMessage());
        }
    }
}