package com.redhat.model;

public class MessageType {
    private String code;
    
    public MessageType(){}
    public MessageType(String code) {
        this.code = code;
    }

    /**
     * @return String return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MessageType [code=" + code + "]";
    }

}
