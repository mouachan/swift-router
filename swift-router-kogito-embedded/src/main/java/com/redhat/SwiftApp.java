package com.redhat;

import javax.ws.rs.core.MediaType;

import com.redhat.model.Message;
import com.redhat.model.Event;
import com.redhat.model.MessageType;
import com.redhat.model.Document;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import org.jboss.logging.Logger;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;


import org.kie.dmn.api.core.DMNResult;
import org.kie.kogito.decision.DecisionModel;
import org.kie.kogito.decision.DecisionModels;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.runtime.QuarkusApplication;




@ApplicationScoped
public class SwiftApp implements QuarkusApplication{
    private static final Logger LOGGER = Logger.getLogger(SwiftApp.class);

    private static String ID_APP = "QUARKUS-EMBEDDED";

    private List<String> results;

    private Message message;

    private DMNResult dmnResult;

    @Channel("embeddedCodeRoutage")
    Emitter<String> embeddedCodeRoutageRequestEmitter;

    @Inject
    DecisionModels decisionModels;


    public int run(String... args) throws Exception {
        DecisionModel router = decisionModels.getDecisionModel("https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF", "router");
        int nbmessages = 1000;
        message = createRandomMessage();
        // if(this.clientQuarkus == null) 
        //     LOGGER.infof("clientQuarkus is null");
        // else LOGGER.infof("clientQuarkus is not null");
        
        //LOGGER.infof("message %s",message);
        Map<String, Object> context = new HashMap<>();
        results = new ArrayList<String>();
        context.put("event", message.getEvent());
        long start = System.currentTimeMillis();
        for(int i=0; i< nbmessages; i++){
            results.add((router.evaluateAll(router.newContext(context))).toString());
            //LOGGER.infof("Result : "+dmnResult);
        }
        
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        LOGGER.infof("Quarkus - Invoking code routage decision service for %s  messages tooks %s ms",nbmessages,elapsedTime);

        for(String res : results)
         embeddedCodeRoutageRequestEmitter.send(res).toCompletableFuture().join();
        embeddedCodeRoutageRequestEmitter.send(createMessagePerf(nbmessages, elapsedTime)).toCompletableFuture().join();
        return 0;
    }

    private Message createRandomMessage(){
        MessageType messageType = new MessageType("MT598");
        Document document = new Document("55{4:33:20C:AA4444//BKL111{5:RE");
        Event event = new Event("DISTRIBUTION","Swift-FIN","GEBABEBBAAA","ECMSBEBBCCB","", messageType,  document);
        return new Message(event);
    }

    private String createMessagePerf(int nbmessages, long elapsedTime){
        return "{\"id\":"+ID_APP+" \"nbmessage\":"+nbmessages+",\"elapsedtime\":"+elapsedTime+"}";
    }

 


}