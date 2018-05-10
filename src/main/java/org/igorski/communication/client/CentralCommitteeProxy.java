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

/**
 * The Central Committee acts as a proxy to the third party service that handles the events.
 * Uses an {@link EventDispatcher} to do the actual sending of the events.
 *
 */
public class CentralCommitteeProxy {

    private static final String ENDPOINT = SnitcherProperties.getValue("central.committee.endpoint");
    private final EventDispatcher eventDispatcher;
    private long sessionId;

    /**
     * By default it creates and sets a {@link WebServerEventDispatcher} to handle the event sending.
     */
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
        eventDispatcher = webTarget.proxy(WebServerEventDispatcher.class);
        return eventDispatcher;
    }

    private void startSession() {
        SessionStarted sessionStarted = eventDispatcher.sessionStarted(new SessionStarted());
        sessionId = sessionStarted.getSessionId();
    }

    /**
     * Marks that the test plan execution has started by registering all the different test IDs
     * @param uniqueTestIds IDs of all the tests that are part of the test plan
     */
    public void testPlanExecutionStarted(Set<String> uniqueTestIds) {
        for (String uniqueId : uniqueTestIds) {
            eventDispatcher.testRegistered(new TestRegistered(System.currentTimeMillis(), uniqueId, sessionId));
        }
    }

    /**
     * Sends a {@link SessionFinished} event.
     */
    public void testPlanExecutionFinished() {
        eventDispatcher.sessionFinished(new SessionFinished(System.currentTimeMillis(), sessionId));
    }

    /**
     * Creates and sends an {@link ExecutionSkipped} event. Sets the test if the ID and the reason why it was skipped.
     *
     * @param uniqueTestId test ID
     * @param reason why the test is skipped
     */
    public void executionSkipped(String uniqueTestId, String reason) {
        ExecutionSkipped executionSkipped = new ExecutionSkipped(System.currentTimeMillis(), uniqueTestId, sessionId, reason);
        eventDispatcher.executionSkipped(executionSkipped);
    }

    /**
     * Sends a {@link TestStarted} event.
     * @param uniqueTestId id of the started test
     */
    public void executionStarted(String uniqueTestId) {
        TestStarted testStarted = new TestStarted(System.currentTimeMillis(), uniqueTestId, sessionId);
        eventDispatcher.testStarted(testStarted);
    }

    /**
     * Sends an {@link TestFinished} event.
     * @param testExecutionStatus the status of the test execution
     * @param uniqueTestId the ID of the test
     */
    public void executionFinished(TestExecutionResult.Status testExecutionStatus, String uniqueTestId) {
        TestFinished testFinished = new TestFinished(System.currentTimeMillis(), uniqueTestId, sessionId,
                testExecutionStatus.toString());
        eventDispatcher.testFinished(testFinished);
    }
}

