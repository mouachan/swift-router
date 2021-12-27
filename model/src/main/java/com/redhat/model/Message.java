package com.redhat.model;

import java.util.UUID;
import java.util.ArrayList;

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

    public void createRandomMessage(){
        UUID uuid = UUID.randomUUID();
        MessageType messageType = new MessageType("MT598");
        Document document = new Document("55{4:33:20C:AA4444//BKL111{5:RE");
        Event event = new Event(uuid.toString(),"DISTRIBUTION","Swift-FIN","GEBABEBBAAA","ECMSBEBBCCB","", messageType,  document);
        this.setEvent(event);
    }

}
