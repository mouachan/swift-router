package com.redhat.model;


import com.fasterxml.jackson.annotation.JsonProperty;


public class Event {
    private String receiverAddress;

    @JsonProperty("TRN")
    private String trn;

    @JsonProperty("messageType")
    private MessageType messageType;

    @JsonProperty("document")
    private Document document;


    public Event(String receiverAddress, String trn, MessageType messageType, Document document) {
        this.receiverAddress = receiverAddress;
        this.trn = trn;
        this.messageType = messageType;
        this.document = document;
    }
    public String getReceiverAddress() {
        return receiverAddress;
    }
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
    public String getTrn() {
        return trn;
    }
    public void setTrn(String trn) {
        this.trn = trn;
    }
    public MessageType getMessageType() {
        return messageType;
    }
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }
    @Override
    public String toString() {
        return "Event [document=" + document + ", messageType=" + messageType + ", receiverAddress=" + receiverAddress
                + ", trn=" + trn + "]";
    }
        
}
