package org.igorski.communication.client;

import org.igorski.model.events.TestStarted;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.UriBuilder;

class EventDispatcherIT {

    private EventDispatcher eventDispatcher;

    @BeforeEach
    public void beforeEachTest() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget webTarget = client.target(UriBuilder.fromPath("http://localhost"));
        eventDispatcher = webTarget.proxy(EventDispatcher.class);
    }

    @Test
    public void shouldSendRequest() {
        TestStarted testStarted = new TestStarted();
        eventDispatcher.testStarted(testStarted);
    }

}