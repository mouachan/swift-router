package com.redhat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty("event")
    private Event event;

    public Message() {
    }

    public Message(Event event) {
        this.event = event;
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

    @Override
    public String toString() {
        return "Message [event=" + event + "]";
    }

}
