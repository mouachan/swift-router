package com.redhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Result {
    @JsonProperty("event")
    private Event event;
    
    @JsonProperty("codeRoutage")
    private List<String> codeRoutage;

    public Result() {
    }

    public Result(Event event, List<String> codeRoutage) {
        this.event = event;
        this.codeRoutage = codeRoutage;
    }

    /**
     * @return Event return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }


    /**
     * @return List<String> return the codeRoutage
     */
    public List<String> getCodeRoutage() {
        return codeRoutage;
    }

    /**
     * @param codeRoutage the codeRoutage to set
     */
    public void setCodeRoutage(List<String> codeRoutage) {
        this.codeRoutage = codeRoutage;
    }

    @Override
    public String toString() {
        return "Result [event=" + event + ", codeRoutage="+codeRoutage+ "]";
    }



}
