package com.redhat.listeners;


import org.springframework.stereotype.Component;

/**
 * This class demonstrates one of the two methods offered by Kogito to inject custom
 * {@link org.kie.dmn.api.core.event.DMNRuntimeEventListener}s in its internal {@link org.kie.dmn.api.core.DMNRuntime}.
 * <p>
 * This is the quickest one to inject a single listener and it works by creating a standard listener class
 * (a class that implements {@link org.kie.dmn.api.core.event.DMNRuntimeEventListener} interface) and annotating it
 * with {@link ApplicationScoped}.
 * <p>
 * The second injection method is explained in {@link ExampleDecisionEventListenerConfig}.
 * All the listeners instantiated with both methods will be injected during the application startup phase.
 */
@Component
public class SwiftRouterDMNRuntimeEventListener extends LoggingDMNRuntimeEventListener {

    public SwiftRouterDMNRuntimeEventListener() {
        super("SwiftRouterDMNRuntimeEventListener");
    }

}
