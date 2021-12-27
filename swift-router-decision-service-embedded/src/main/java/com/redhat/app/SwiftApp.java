package com.redhat.app;


import com.redhat.model.Message;


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

    @Channel("embeddedCodeRoutage")
    Emitter<String> embeddedCodeRoutageRequestEmitter;

    @Inject
    DecisionModels decisionModels;


    public int run(String... args) throws Exception {
        DecisionModel router = decisionModels.getDecisionModel("https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF", "router");
        int nbmessages = Integer.parseInt(args[0]);
        long start, end, elapsedTime = 0;
        message = new Message();
        message.createRandomMessage();

        Map<String, Object> context = new HashMap<>();
        results = new ArrayList<String>();
        context.put("event", message.getEvent());
        start = System.currentTimeMillis();
        for(int i=0; i< nbmessages; i++){
            results.add((router.evaluateAll(router.newContext(context))).toString());
        }
        
        end = System.currentTimeMillis();
        elapsedTime = end - start;
        LOGGER.infof("Embedded mode - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,(float)(elapsedTime/1000.0));

        for(String res : results)
         embeddedCodeRoutageRequestEmitter.send(res).toCompletableFuture().join();
        embeddedCodeRoutageRequestEmitter.send(createMessagePerf(nbmessages, (float)(elapsedTime/1000.0))).toCompletableFuture().join();
        return 0;
    }


    private String createMessagePerf(int nbmessages, float elapsedTime){
        return "{\"service type\":\"quarkus-embedded\",\"nb messages\":"+nbmessages+",\"elapsed time\":"+elapsedTime+"}";
    }

 


}