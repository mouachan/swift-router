package com.redhat.producer;

import java.util.UUID;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

import com.redhat.model.*;

import org.eclipse.microprofile.rest.client.inject.RestClient;


import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.mutiny.Multi;




@Path("/swift")
public class SwiftResource{
    private static final Logger LOGGER = Logger.getLogger(SwiftResource.class);


    @Channel("swift-requests")
    Emitter<Message> swiftRequestEmitter;

    @POST
    @Path("/request")
    @Produces(MediaType.TEXT_PLAIN)
    public String createRequest() {
        Message message = new Message();
        message.createRandomMessage();
        swiftRequestEmitter.send(message);
        return message.getEvent().getId();
    }

    @Channel("codeRoutage")
    Multi<String> codeRoutage;

   
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS) 
    public Multi<String> stream() {
        return codeRoutage.log();
    }

    private Message createRandomMessage(){
        UUID uuid = UUID.randomUUID();
        MessageType messageType = new MessageType("MT598");
        Document document = new Document("55{4:33:20C:AA4444//BKL111{5:RE");
        Event event = new Event(uuid.toString(),"DISTRIBUTION","Swift-FIN","GEBABEBBAAA","ECMSBEBBCCB","", messageType,  document);
        return new Message(event);
    }

}