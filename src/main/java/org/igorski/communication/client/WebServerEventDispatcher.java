package org.igorski.communication.client;

import org.igorski.model.events.ExecutionSkipped;
import org.igorski.model.events.SessionFinished;
import org.igorski.model.events.SessionStarted;
import org.igorski.model.events.TestFinished;
import org.igorski.model.events.TestRegistered;
import org.igorski.model.events.TestStarted;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Sends the events to a RESTful server.
 */
@Path("/api/events")
public interface WebServerEventDispatcher extends EventDispatcher {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/started")
    SessionStarted sessionStarted(SessionStarted sessionStarted);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/finished")
    SessionFinished sessionFinished(SessionFinished sessionFinished);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/test/started")
    TestStarted testStarted(TestStarted testStarted);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/test/finished")
    TestFinished testFinished(TestFinished testFinished);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/test/skipped")
    ExecutionSkipped executionSkipped(ExecutionSkipped executionSkipped);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/session/registered")
    TestRegistered testRegistered(TestRegistered testRegistered);
}
