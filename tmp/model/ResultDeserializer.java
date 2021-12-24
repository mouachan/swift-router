package com.redhat.model;



import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import com.redhat.model.Result;

public class ResultDeserializer extends JsonbDeserializer<Result> {
    public ResultDeserializer() {
        super(Result.class);
    }
}