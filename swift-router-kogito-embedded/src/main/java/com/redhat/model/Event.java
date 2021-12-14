package com.redhat.model;


import com.fasterxml.jackson.annotation.JsonProperty;


public class Event {
    private String direction;

    private String networkProtocol;

    private String receiverAddress;

    private String senderAddress;

    @JsonProperty("TRN")
    private String trn;

    @JsonProperty("messageType")
    private MessageType messageType;

    @JsonProperty("document")
    private Document document;

    
    public Event(String direction, String networkProtocol, String receiverAddress, String senderAddress, String trn,
            MessageType messageType, Document document) {
        this.direction = direction;
        this.networkProtocol = networkProtocol;
        this.receiverAddress = receiverAddress;
        this.senderAddress = senderAddress;
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
        

    /**
     * @return String return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return String return the networkProtocol
     */
    public String getNetworkProtocol() {
        return networkProtocol;
    }

    /**
     * @param networkProtocol the networkProtocol to set
     */
    public void setNetworkProtocol(String networkProtocol) {
        this.networkProtocol = networkProtocol;
    }

    /**
     * @return String return the senderAddress
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    /**
     * @param senderAddress the senderAddress to set
     */
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
    @Override
    public String toString() {
        return "Event [direction=" + direction + ", document=" + document + ", messageType=" + messageType
                + ", networkProtocol=" + networkProtocol + ", receiverAddress=" + receiverAddress + ", senderAddress="
                + senderAddress + ", trn=" + trn + "]";
    }

}
