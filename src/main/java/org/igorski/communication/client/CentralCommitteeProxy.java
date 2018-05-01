package org.igorski.communication.client;

import org.igorski.SnitcherProperties;
import org.igorski.model.events.ExecutionSkipped;
import org.igorski.model.events.SessionFinished;
import org.igorski.model.events.SessionStarted;
import org.igorski.model.events.TestFinished;
import org.igorski.model.events.TestRegistered;
import org.igorski.model.events.TestStarted;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.platform.engine.TestExecutionResult;

import javax.ws.rs.core.UriBuilder;
import java.util.Set;

public class CentralCommitteeProxy {

    private static final String ENDPOINT = SnitcherProperties.getValue("central.committee.endpoint");
    private final EventDispatcher eventDispatcher;
    private long sessionId;

    public CentralCommitteeProxy() {
        this(getEventDispatcher());
    }

    CentralCommitteeProxy(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        startSession();
    }

    private static EventDispatcher getEventDispatcher() {
        EventDispatcher eventDispatcher;
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget webTarget = client.target(UriBuilder.fromPath(ENDPOINT));
        eventDispatcher = webTarget.proxy(EventDispatcher.class);
        return eventDispatcher;
    }

    private void startSession() {
        SessionStarted sessionStarted = eventDispatcher.sessionStarted(new SessionStarted());
        sessionId = sessionStarted.getSessionId();
    }

    public void testPlanExecutionStarted(Set<String> uniqueIds) {
        for (String uniqueId : uniqueIds) {
            eventDispatcher.testRegistered(new TestRegistered(System.currentTimeMillis(), uniqueId, sessionId));
        }
    }

    public void testPlanExecutionFinished() {
        eventDispatcher.sessionFinished(new SessionFinished(System.currentTimeMillis(), sessionId));
    }

    public void executionSkipped(String uniqueId, String reason) {
        ExecutionSkipped executionSkipped = new ExecutionSkipped(System.currentTimeMillis(), uniqueId, sessionId, reason);
        eventDispatcher.executionSkipped(executionSkipped);
    }

    public void executionStarted(String uniqueId) {
        TestStarted testStarted = new TestStarted(System.currentTimeMillis(), uniqueId, sessionId);
        eventDispatcher.testStarted(testStarted);
    }

    public void executionFinished(TestExecutionResult.Status testExecutionStatus, String uniqueId) {
        TestFinished testFinished = new TestFinished(System.currentTimeMillis(), uniqueId, sessionId,
                testExecutionStatus.toString());
        eventDispatcher.testFinished(testFinished);
    }
}

