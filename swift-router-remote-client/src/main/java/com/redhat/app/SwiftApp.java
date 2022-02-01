package com.redhat.app;




import com.redhat.model.Message;
import com.redhat.rest.client.SwiftQuarkusRemoteService;
import com.redhat.rest.client.SwiftSpringBootRemoteService;


import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.quarkus.runtime.QuarkusApplication;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;



@ApplicationScoped
public class SwiftApp implements QuarkusApplication{
    private static final Logger LOGGER = Logger.getLogger(SwiftApp.class);

    private String ID_APP;

    @Channel("qCodeRoutage")
    Emitter<String> qCodeRoutageRequestEmitter;

    @Channel("sbCodeRoutage")
    Emitter<String> sbCodeRoutageRequestEmitter;

    @Inject
    @RestClient
    SwiftQuarkusRemoteService clientQuarkus;

    @Inject
    @RestClient
    SwiftSpringBootRemoteService clientSpringBoot;



    private Message message;

    private List<String> results;

    @ConfigProperty(name = "SERVICE_TYPE")
    private String serviceType;

    @ConfigProperty(name = "NB_MESSAGES")
    private int nbmessages;


    public int run(String... args) throws Exception {
            // nbmessages = Integer.parseInt(args[0]);
            // serviceType = args[1];
            long start, end, elapsedTime = 0;
            message = new Message();
            message.createRandomMessage();
            LOGGER.infof("message %s",message);
            if(serviceType.equals("quarkus")){
                results = new ArrayList<String>();
                start = System.currentTimeMillis();
                for(int i=0; i< nbmessages; i++){;
                    results.add((this.clientQuarkus.callDMNCodesRoutage(message)).toString());
                }
                end = System.currentTimeMillis();
                elapsedTime = end - start;
                LOGGER.infof("Quarkus - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,(float)(elapsedTime/1000.0));            
                for(String res : results)
                    qCodeRoutageRequestEmitter.send(res).toCompletableFuture().join();
                qCodeRoutageRequestEmitter.send(createMessagePerf(nbmessages, (float)(elapsedTime/1000.0))).toCompletableFuture().join();
            } else if(serviceType.equals("springboot")){  
                results = new ArrayList<String>();
                start = System.currentTimeMillis();
                for(int i=0; i< nbmessages; i++){;
                    results.add((this.clientSpringBoot.callDMNCodesRoutage(message)).toString());
                }
                end = System.currentTimeMillis();
                elapsedTime = end - start;
                LOGGER.infof("SpringBoot - Invoking code routage decision service for %s  messages tooks %s s",nbmessages,(float)(elapsedTime/1000.0));

                for(String res : results)
                    sbCodeRoutageRequestEmitter.send(res).toCompletableFuture().join();
                sbCodeRoutageRequestEmitter.send(createMessagePerf(nbmessages, (float)(elapsedTime/1000.0))).toCompletableFuture().join();
            } else {
                LOGGER.infof("Argument %s must be quarkus or springboot ", serviceType);
            }
        return 0;
    }



    private String createMessagePerf(int nbmessages, float elapsedTime){
        return "{ \"service type\":\""+serviceType+"-remote\""+",\"nb messages\":"+nbmessages+",\"elapsedtime\":"+elapsedTime+"}";
    }


}