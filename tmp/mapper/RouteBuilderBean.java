package com.redhat.mapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
//import com.redhat.utils.camel3.KogitoProcessorFactory;
import com.redhat.utils.camel3.WhereToAggregationStrategy; 

@ApplicationScoped
public class RouteBuilderBean extends RouteBuilder {

    // @Inject
    // KogitoProcessorFactory kogitoProcessor;

    Processor kogitoDMNEvaluate;
    
    // @PostConstruct
    // public void init() {
    //     kogitoDMNEvaluate = kogitoProcessor.decisionProcessor("ns1", "router");
    // }

    AggregationStrategy aggregationStrategy = new WhereToAggregationStrategy("CATCH_ALL");

    @Override
    public void configure() throws Exception {

        from("kafka:input")
            .to("atlasmap:atlasmap-mapping.adm").unmarshal().json()
            //.process(kogitoDMNEvaluate) // <== Rules as DMN decisions to decide which Kakfa topic queue to be sent to.
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader("Content-Type", constant("application/json") )
            .to("http://localhost:8080/router")
            .to("log:org.drools.demo?level=DEBUG&showAll=true&multiline=true")
            .to("kafka:output")
        ;

    //     // produces messages to kafka
    //   from("timer:foo?period={{timer.period}}&delay={{timer.delay}}")
    //             .routeId("FromDMN2Kafka")
    //             .setBody().simple("Message #${exchangeProperty.CamelTimerCounter}")
    //             .to("kafka:{{kafka.topic.name}}")
    //             .log("Message sent correctly sent to the topic! : \"${body}\" ");

    //   // kafka consumer
    //   from("kafka:{{kafka.topic.name}}")
    //           .routeId("FromKafka2DMN")
    //           .log("Received : \"${body}\"")
    //           .to("atlasmap:atlasmap-mapping.adm").unmarshal().json()
    //           .process(kogitoDMNEvaluate); // <== Rules as DMN decisions to decide which Kakfa topic queue to be sent to.
  //}
    }
}
