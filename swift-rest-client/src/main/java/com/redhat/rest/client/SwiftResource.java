package com.redhat.rest.client;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.MediaType;

import com.redhat.model.Message;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.inject.Inject;


@Path("/swift")
public class SwiftResource {

    @Inject
    @RestClient
    SwiftRemoteService client;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getCodesRoutage(Message message) {
        System.out.println(this.client.getCodesRoutage(message));
        return "Hello RESTEasy";
    }
}