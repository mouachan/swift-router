package com.redhat.rest.client;



import javax.ws.rs.core.MediaType;

import com.redhat.model.Message;
import com.redhat.model.Event;
import com.redhat.model.MessageType;
import com.redhat.model.Document;


import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.ArrayList;

import javax.inject.Inject;
import org.jboss.logging.Logger;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
//import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.QuarkusApplication;
import javax.enterprise.context.ApplicationScoped;



@ApplicationScoped
public class SwiftApp implements QuarkusApplication{
    private static final Logger LOGGER = Logger.getLogger(SwiftApp.class);

    @Channel("sbCodeRoutage")
    Emitter<String> sbCodeRoutageRequestEmitter;

    @Inject
    @RestClient
    SwiftSpringBootRemoteService clientSpringboot;

    private String result;

    private Message message;


    
    public int run(String... args) throws Exception {
        int nbmessages = 1000;
        message = createRandomMessage();
        // if(this.clientQuarkus == null) 
        //     LOGGER.infof("clientQuarkus is null");
        // else LOGGER.infof("clientQuarkus is not null");
        for(int i=0; i< 100; i++){
            this.clientSpringboot.callDMNCodesRoutage(message);
        }

        LOGGER.infof("message %s",message);
        long start = System.currentTimeMillis();
        for(int i=0; i< nbmessages; i++){
            result = this.clientSpringboot.callDMNCodesRoutage(message);
            sbCodeRoutageRequestEmitter.send(result);
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        LOGGER.infof("SpringBoot - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,elapsedTime/1000);
        return 0;
    }

    private Message createRandomMessage(){
        MessageType messageType = new MessageType("MT012");
        Document document = new Document("r{4:5103:EBA7{5:6");
        Event event = new Event("BNPAFRPP", "Test", messageType,  document);
        return new Message(event);
    }


}