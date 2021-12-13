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


    @Channel("qCodeRoutage")
    Emitter<String> qCodeRoutageRequestEmitter;

    @Inject
    @RestClient
    SwiftQuarkusRemoteService clientQuarkus;


    private String result;

    private Message message;


    
    public int run(String... args) throws Exception {
        int nbmessages = 1000;
        message = createRandomMessage();
        // if(this.clientQuarkus == null) 
        //     LOGGER.infof("clientQuarkus is null");
        // else LOGGER.infof("clientQuarkus is not null");
        
        for(int i=0; i< 100; i++){
            this.clientQuarkus.callDMNCodesRoutage(message);
        }

        LOGGER.infof("message %s",message);

        long start = System.currentTimeMillis();
        for(int i=0; i< nbmessages; i++){
            result = this.clientQuarkus.callDMNCodesRoutage(message);
            qCodeRoutageRequestEmitter.send(result);
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        qCodeRoutageRequestEmitter.send(createMessagePerf(nbmessages, elapsedTime));
        LOGGER.infof("Quarkus - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,elapsedTime/1000);
        return 0;
    }

    private Message createRandomMessage(){
        MessageType messageType = new MessageType("MT598");
        Document document = new Document("55{4:33:20C:AA4444//BKL111{5:RE");
        Event event = new Event("DISTRIBUTION","Swift-FIN","GEBABEBBAAA","ECMSBEBBCCB","", messageType,  document);
        return new Message(event);
    }

    private String createMessagePerf(int nbmessages, long elapsedTime){
        return "{ \"nbmessage\":"+nbmessages+",\"elapsedtime\":"+elapsedTime+"}";
    }


}