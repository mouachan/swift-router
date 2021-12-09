package com.redhat.rest.client;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import org.jboss.resteasy.annotations.jaxrs.PathParam;


import javax.ws.rs.core.MediaType;

import com.redhat.model.Message;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.ArrayList;

import javax.inject.Inject;
import org.jboss.logging.Logger;


@Path("/swift")
public class SwiftResource {
    private static final Logger LOGGER = Logger.getLogger(SwiftResource.class);


    @Inject
    @RestClient
    SwiftQuarkusRemoteService clientQuarkus;

    @Inject
    @RestClient
    SwiftSpringBootRemoteService clientSpringboot;

    private List results;

    @POST
    @Path("/onecall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getCodesRoutage(Message message) {
        String result = this.clientSpringboot.callDMNCodesRoutage(message);
        return result;
    }

    @POST
    @Path("/{nbmessages}/multicall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getCodesRoutage(@PathParam("nbmessages") int nbmessages, Message message) {
        LOGGER.infof("Start ");

        long start = System.currentTimeMillis();
        // some time passes
        results = new ArrayList<String>();
        for(int i=0; i< nbmessages; i++)
            results.add(this.clientQuarkus.callDMNCodesRoutage(message));
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        LOGGER.infof("Quarkus - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,elapsedTime/1000);

        start = System.currentTimeMillis();

        results = new ArrayList<String>();
        for(int i=0; i< nbmessages; i++)
            results.add(this.clientSpringboot.callDMNCodesRoutage(message));
        end = System.currentTimeMillis();
        elapsedTime = end - start;
        LOGGER.infof("SpringBoot - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,elapsedTime/1000);
        return results;
    }
}