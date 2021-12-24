package com.redhat.util;




import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import com.redhat.model.Message;

public class MessageDeserializer extends JsonbDeserializer<Message> {
    public MessageDeserializer() {
        super(Message.class);
    }
}