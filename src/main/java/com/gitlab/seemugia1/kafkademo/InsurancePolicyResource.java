package com.gitlab.seemugia1.kafkademo;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import java.util.List;

@Path("/policies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InsurancePolicyResource {

    @Inject
    InsurancePolicyRepository repository;

    @GET
    public List<InsurancePolicy> getAll() {
        return repository.listAll();
    }

    @GET
    @Path("/{id}")
    public Response geByID(@PathParam("id") Long id) {
        return Response.ok(repository.findById(id)).build();
    }

    @POST
    @Transactional
    public Response create(InsurancePolicy policy) {
        repository.persist(policy);
        return Response.status(Response.Status.CREATED).entity(policy).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, InsurancePolicy policy) {
        policy.setId(id);
        repository.getEntityManager().merge(policy);
        return Response.ok(policy).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        repository.deleteById(id);
        return Response.noContent().build();
    }
}
