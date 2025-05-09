package com.gitlab.seemugia1.kafkademo;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(InsurancePolicyResource.class)
public class InsurancePolicyResourceTest {

    @Test
    public void testCreateGetUpdateDeletePolicy() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyHolderName("REST User");
        policy.setCoverageAmount(BigDecimal.valueOf(100000));
        policy.setPremium(BigDecimal.valueOf(500));
        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusYears(1));
        policy.setStatus("ACTIVE");

        // Create
        Long id = given()
                .contentType("application/json")
                .body(policy)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("policyHolderName", is("REST User"))
                .extract()
                .path("id");

        // Get
        given()
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(200)
                .body("id", is(id.intValue()));

        // Update
        policy.setPolicyHolderName("REST User Updated");
        given()
                .contentType("application/json")
                .body(policy)
                .when()
                .put("/{id}", id)
                .then()
                .statusCode(200)
                .body("policyHolderName", is("REST User Updated"));

        // Delete
        given()
                .when()
                .delete("/{id}", id)
                .then()
                .statusCode(204);

        // Confirm Deletion
        given()
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetNonExistentPolicy() {
        given()
                .when()
                .get("/999999")
                .then()
                .statusCode(404);
    }
}
