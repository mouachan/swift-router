package com.redhat.rest.client;



import javax.ws.rs.core.MediaType;

import com.redhat.model.Message;
import com.redhat.model.Event;
import com.redhat.model.MessageType;
import com.redhat.model.Document;


import org.eclipse.microprofile.rest.client.inject.RestClient;


import javax.inject.Inject;
import org.jboss.logging.Logger;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import io.quarkus.runtime.QuarkusApplication;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;



@ApplicationScoped
public class SwiftApp implements QuarkusApplication{
    private static final Logger LOGGER = Logger.getLogger(SwiftApp.class);

    @Channel("sbCodeRoutage")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 100000)
    Emitter<String> sbCodeRoutageRequestEmitter;

    @Inject
    @RestClient
    SwiftSpringBootRemoteService clientSpringboot;

    private List<String> results;

    private Message message;


    
    public int run(String... args) throws Exception {
        int nbmessages = 1000;
        message = createRandomMessage();
        // if(this.clientQuarkus == null) 
        //     LOGGER.infof("clientQuarkus is null");
        // else LOGGER.infof("clientQuarkus is not null");

        LOGGER.infof("message %s",message);
        results = new ArrayList<String>();
        long start = System.currentTimeMillis();
        for(int i=0; i< nbmessages; i++){
            results.add((this.clientSpringboot.callDMNCodesRoutage(message)).toString());
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        LOGGER.infof("SpringBoot - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,elapsedTime/1000);
        LOGGER.infof("number of message in the list %s"+results.size());
        int i = 0;
        for(String res : results){
            //LOGGER.infof("resultat number %s %s",i, res);
            sbCodeRoutageRequestEmitter.send(res).toCompletableFuture().join();
                // whenComplete((success, failure) -> {
                //     if (failure != null) {
                //         System.out.println("D'oh! " + failure.getMessage());
                //     } 
                // });
                // i++;
            }
        sbCodeRoutageRequestEmitter.send(createMessagePerf(nbmessages, elapsedTime)).toCompletableFuture().join();
            // whenComplete((success, failure) -> {
            //     if (failure != null) {
            //         System.out.println("D'oh! " + failure.getMessage());
            //     } 
            // });
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