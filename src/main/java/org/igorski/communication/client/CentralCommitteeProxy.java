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
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import javax.ws.rs.core.UriBuilder;
import java.util.Set;

public class CentralCommitteeProxy {

    private static final String ENDPOINT = SnitcherProperties.getValue("central.committee.endpoint");
    private final EventDispatcher eventDispatcher;
    private long sessionId;

    public CentralCommitteeProxy() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget webTarget = client.target(UriBuilder.fromPath(ENDPOINT));
        eventDispatcher = webTarget.proxy(EventDispatcher.class);
        sessionId = eventDispatcher.sessionStarted(new SessionStarted()).getSessionId();
    }

    CentralCommitteeProxy(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void testPlanExecutionStarted(Set<String> uniqueIds) {
        for(String uniqueId : uniqueIds) {
            eventDispatcher.testRegistered(new TestRegistered(System.currentTimeMillis(), uniqueId, sessionId));
        }
    }

    public void testPlanExecutionFinished(TestPlan testPlan) {
        eventDispatcher.sessionFinished(new SessionFinished(System.currentTimeMillis(), sessionId));
    }

    public void dynamicTestRegistered(TestIdentifier testIdentifier) {

    }

    public void executionSkipped(String uniqueId, String reason) {
        ExecutionSkipped executionSkipped = new ExecutionSkipped(System.currentTimeMillis(), uniqueId,
                sessionId, reason);
        eventDispatcher.executionSkipped(executionSkipped);
    }

    public void executionStarted(String uniqueId) {
        TestStarted testStarted = new TestStarted(System.currentTimeMillis(), uniqueId, sessionId);
        eventDispatcher.testStarted(testStarted);
    }

    public void executionFinished(TestExecutionResult testExecutionResult, String uniqueId) {
        TestFinished testFinished = new TestFinished(System.currentTimeMillis(), uniqueId,
                sessionId, testExecutionResult.getStatus().toString());
        eventDispatcher.testFinished(testFinished);
    }

    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {

    }
}

