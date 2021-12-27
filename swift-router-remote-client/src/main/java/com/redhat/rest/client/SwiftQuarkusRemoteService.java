package com.redhat.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.redhat.model.Message;

import javax.ws.rs.POST;
import javax.ws.rs.Path;


@RegisterRestClient
public interface SwiftQuarkusRemoteService {

    @POST
    @Path("/router")
    String callDMNCodesRoutage(Message message);


}
