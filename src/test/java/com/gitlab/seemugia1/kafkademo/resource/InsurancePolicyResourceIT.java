package com.gitlab.seemugia1.kafkademo.resource;

import com.gitlab.seemugia1.kafkademo.domain.model.InsurancePolicy;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import java.math.BigDecimal;

@QuarkusTest
@TestHTTPEndpoint(InsurancePolicyResource.class)
class InsurancePolicyResourceIT {

    @Test
    void testCreateAndGetPolicy() {
        InsurancePolicy policy = new InsurancePolicy();
        BigDecimal value = new BigDecimal(100000.0);
        policy.setPolicyHolderName("Alice");
        policy.setCoverageAmount(value);
        policy.setPremium(value);
        policy.setStatus("ACTIVE");

        // Create
        given()
                .contentType("application/json")
                .body(policy)
                .when()
                .post()
                .then()
                .statusCode(201);

        // Get all
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void testGetNonExistentPolicy() {
        given()
                .when()
                .get("/9999")
                .then()
                .statusCode(404);
    }

    @Test
    void testDeletePolicy_NotFound() {
        given()
                .when()
                .delete("/9999")
                .then()
                .statusCode(404);
    }
}
