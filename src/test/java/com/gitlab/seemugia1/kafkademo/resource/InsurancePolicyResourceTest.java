package com.gitlab.seemugia1.kafkademo.resource;

import com.gitlab.seemugia1.kafkademo.domain.model.InsurancePolicy;
import com.gitlab.seemugia1.kafkademo.repository.InsurancePolicyRepository;
import com.gitlab.seemugia1.kafkademo.service.InsurancePolicyKafkaService;

import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyResourceUnitTest {

    InsurancePolicyResource resource;
    FakeRepository fakeRepo;
    FakeKafkaService fakeKafka;

    @BeforeEach
    void setup() {
        fakeRepo = new FakeRepository();
        fakeKafka = new FakeKafkaService();
        resource = new InsurancePolicyResource();
        resource.repository = fakeRepo;
        resource.kafkaService = fakeKafka;
    }

    @Test
    void testCreatePolicy_Success() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyHolderName("Alice");
        policy.setCoverageAmount(BigDecimal.valueOf(100000));
        policy.setPremium(BigDecimal.valueOf(1000));
        policy.setStatus("ACTIVE");

        Response response = resource.create(policy);
        assertEquals(201, response.getStatus());
        assertEquals(1, fakeRepo.storage.size());
        assertEquals("created", fakeKafka.lastAction);
    }

    @Test
    void testGetPolicy_NotFound() {
        Response response = resource.getById(99L);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testUpdatePolicy_NotFound() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyHolderName("Bob");
        Response response = resource.update(42L, policy);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testDeletePolicy_NotFound() {
        Response response = resource.delete(99L);
        assertEquals(404, response.getStatus());
    }

    // ==== Fake Repository Stub ====
    static class FakeRepository extends InsurancePolicyRepository {
        Map<Long, InsurancePolicy> storage = new HashMap<>();
        long idSequence = 1L;

        @Override
        public void persist(InsurancePolicy policy) {
            policy.setId(idSequence++);
            storage.put(policy.getId(), policy);
        }

        @Override
        public InsurancePolicy findById(Long id) {
            return storage.get(id);
        }

        @Override
        public List<InsurancePolicy> listAll() {
            return new ArrayList<>(storage.values());
        }

        @Override
        public void delete(InsurancePolicy policy) {
            storage.remove(policy.getId());
        }

        @Override
        public jakarta.persistence.EntityManager getEntityManager() {
            throw new UnsupportedOperationException("Not needed for this test");
        }
    }

    static class FakeKafkaService extends InsurancePolicyKafkaService {
        String lastAction;
        InsurancePolicy lastPolicy;

        @Override
        public void sendPolicyEvent(String action, InsurancePolicy policy) {
            this.lastAction = action;
            this.lastPolicy = policy;
        }
    }
}
