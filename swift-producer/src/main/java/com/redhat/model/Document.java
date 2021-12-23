
package com.redhat.model;

public class Document {
    private String data;

    public Document(){}

    public Document(String data) {
        this.data = data;
    }

    /**
     * @return String return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Document [data=" + data + "]";
    }

}
