package com.gitlab.seemugia1.kafkademo;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaProducerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class);

    @Outgoing("policy-producer")
    public void produce(InsurancePolicy policy) {
        LOGGER.info("Producing policy update: " + policy);
    }
}
