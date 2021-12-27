package com.redhat;


import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;


import org.eclipse.microprofile.rest.client.inject.RestClient;


import org.jboss.logging.Logger;

import com.redhat.rest.client.*;

import com.redhat.model.Message;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Blocking;



@ApplicationScoped
public class SwiftProcessor{
    private static final Logger LOGGER = Logger.getLogger(SwiftProcessor.class);


    @Inject
    @RestClient
    SwiftQuarkusRemoteService clientQuarkus;

    @Incoming("requests")
    @Outgoing("codeRoutage")
    @Blocking
    public String process(Message message) throws InterruptedException {
        LOGGER.infof("message received %s",message.toString());
        String result = this.clientQuarkus.callDMNCodesRoutage(message); 
        LOGGER.infof("Codes Routage  %s",result);
        return result;
    }

}