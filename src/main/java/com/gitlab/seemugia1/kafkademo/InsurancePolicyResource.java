package com.gitlab.seemugia1.kafkademo;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@Path("/policies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InsurancePolicyResource {

    private static final Logger LOGGER = Logger.getLogger(InsurancePolicyResource.class);

    @Inject
    InsurancePolicyRepository repository;

    @Inject
    InsurancePolicyKafkaService kafkaService;

    @GET
    public Response getAll() {
        List<InsurancePolicy> policies = repository.listAll();
        return Response.ok(policies).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        InsurancePolicy policy = repository.findById(id);
        if (policy == null) {
            LOGGER.warnf("Policy with ID %d not found", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Policy not found.").build();
        }
        return Response.ok(policy).build();
    }

    @POST
    public Response create(InsurancePolicy policy) {
        try {
            createPolicy(policy);
            kafkaService.sendPolicyEvent("created", policy);
            return Response.status(Response.Status.CREATED).entity(policy).build();
        } catch (Exception e) {
            LOGGER.error("Error creating policy", e);
            return Response.serverError().entity("Failed to create policy. Please try again later.").build();
        }
    }

    @Transactional
    void createPolicy(InsurancePolicy policy) {
        repository.persist(policy);
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, InsurancePolicy policy) {
        try {
            InsurancePolicy existing = repository.findById(id);
            if (existing == null) {
                LOGGER.warnf("Update failed. Policy with ID %d not found", id);
                return Response.status(Response.Status.NOT_FOUND).entity("Policy not found.").build();
            }
            policy.setId(id);
            updatePolicy(policy);
            kafkaService.sendPolicyEvent("updated", policy);
            return Response.ok(policy).build();
        } catch (Exception e) {
            LOGGER.error("Error updating policy", e);
            return Response.serverError().entity("Failed to update policy. Please try again later.").build();
        }
    }

    @Transactional
    void updatePolicy(InsurancePolicy policy) {
        repository.getEntityManager().merge(policy);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            InsurancePolicy policy = repository.findById(id);
            if (policy == null) {
                LOGGER.warnf("Delete failed. Policy with ID %d not found", id);
                return Response.status(Response.Status.NOT_FOUND).entity("Policy not found.").build();
            }
            kafkaService.sendPolicyEvent("deleted", policy);
            return Response.noContent().build();
        } catch (Exception e) {
            LOGGER.error("Error deleting policy", e);
            return Response.serverError().entity("Failed to delete policy. Please try again later.").build();
        }
    }

    @Transactional
    void deletePolicy(InsurancePolicy policy) {
        repository.delete(policy);
    }
}
