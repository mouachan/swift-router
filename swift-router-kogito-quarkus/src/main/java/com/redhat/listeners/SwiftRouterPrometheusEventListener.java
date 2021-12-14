package com.redhat.listeners;

import org.kie.dmn.api.core.event.AfterEvaluateAllEvent;
import org.kie.dmn.api.core.event.AfterEvaluateContextEntryEvent;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionEvent;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionTableEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateAllEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateContextEntryEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateDecisionEvent;
import org.kie.dmn.api.core.event.BeforeEvaluateDecisionTableEvent;
import org.kie.dmn.api.core.event.DMNRuntimeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

public class SwiftRouterPrometheusEventListener implements DMNRuntimeEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(SwiftRouterPrometheusEventListener.class);
    private final String name;


    protected final Counter numberOfLoanApplicationsApproved = Counter.build()
            .name("Number of code ")
            .help("Approved loan applications")
            .labelNames("app_id","note", "score","rate","months" )
            .register();

    public SwiftRouterPrometheusEventListener(String name) {
        this.name = name;
    }

    @Override
    public void beforeEvaluateDecision(BeforeEvaluateDecisionEvent event) {
        //log("BeforeEvaluateDecisionEvent");
    }

    @Override
    public void afterEvaluateDecision(AfterEvaluateDecisionEvent event) {
        //log("Decision name : "+event.getDecision().getName());
    }

    @Override
    public void beforeEvaluateContextEntry(BeforeEvaluateContextEntryEvent event) {
       // log("BeforeEvaluateContextEntryEvent");
    }

    @Override
    public void afterEvaluateContextEntry(AfterEvaluateContextEntryEvent event) {
       // log("AfterEvaluateContextEntryEvent");
    }

    @Override
    public void beforeEvaluateDecisionTable(BeforeEvaluateDecisionTableEvent event) {
       // log("BeforeEvaluateDecisionTableEvent");
    }

    @Override
    public void afterEvaluateDecisionTable(AfterEvaluateDecisionTableEvent event) {
        // log("node   : "+event.getNodeName());
        // log("   table  : "+event.getDecisionTableName());
        // for(int i=0; i< event.getSelected().size();i++)
        //     log("       line number : "+event.getSelected().get(i));
    }

    @Override
    public void beforeEvaluateAll(BeforeEvaluateAllEvent event) {
        //log("BeforeEvaluateAllEvent");
    }

    @Override
    public void afterEvaluateAll(AfterEvaluateAllEvent event) {
        //log("AfterEvaluateAllEvent");
    }

    private void log(String event) {
        LOG.debug("{} received by {}", event, name);
    }

}
