package com.redhat.listeners;


import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.dmn.config.CachedDecisionEventListenerConfig;

/**
 * This class demonstrates one of the two methods offered by Kogito to inject custom
 * {@link org.kie.dmn.api.core.event.DMNRuntimeEventListener}s in its internal {@link org.kie.dmn.api.core.DMNRuntime}.
 * <p>
 * It works by creating a bean that implements {@link org.kie.kogito.decision.DecisionEventListenerConfig} interface
 * (which returns the list of desired instances in the {@link org.kie.kogito.decision.DecisionEventListenerConfig#listeners()}
 * method) and annotating it with {@link ApplicationScoped}.
 * <p>
 * We're extending {@link CachedDecisionEventListenerConfig} instead of implementing the interface directly
 * only because the intermediate class provides a utility method to register listener instances. This is the
 * suggested way if the Config class is not supposed to contain any extra logic.
 * <p>
 * The second injection method is explained in {@link ExampleDMNRuntimeEventListener}.
 * All the listeners instantiated with both methods will be injected during the application startup phase.
 */
@ApplicationScoped
public class SwiftRouterDecisionEventListenerConfig extends CachedDecisionEventListenerConfig {

    public SwiftRouterDecisionEventListenerConfig() {
        register(new LoggingDMNRuntimeEventListener("SwiftRouterDecisionEventListener"));
    }

}
