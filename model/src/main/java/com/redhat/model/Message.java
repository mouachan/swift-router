package com.redhat.model;


public class Message {
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
        return "Message [event=" + event +"]";
    }



}
