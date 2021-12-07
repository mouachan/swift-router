package com.redhat.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.redhat.model.Message;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Set;

@RegisterRestClient
public interface SwiftRemoteService {

    @POST
    @Path("/router")
    String getCodesRoutage(Message message);


}
