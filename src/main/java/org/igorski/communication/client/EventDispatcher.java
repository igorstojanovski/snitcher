package org.igorski.communication.client;

import org.igorski.model.events.ExecutionSkipped;
import org.igorski.model.events.SessionFinished;
import org.igorski.model.events.SessionStarted;
import org.igorski.model.events.TestFinished;
import org.igorski.model.events.TestRegistered;
import org.igorski.model.events.TestStarted;

/**
 * Sends the events to an event reporting facility. Implementation choose where and how to send the events. It could
 * be a web server, xml based, json based, or simply write down everything into a file on the local file system.
 */
public interface EventDispatcher {

    /**
     * Sends a session started event.
     *
     * @param sessionStarted the session started event
     * @return the session started event response. This object is expected to contain the session ID of the session.
     */
    SessionStarted sessionStarted(SessionStarted sessionStarted);

    /**
     * Sends a session finished event.
     *
     * @param sessionFinished the session finished event
     * @return the session finished event response
     */
    SessionFinished sessionFinished(SessionFinished sessionFinished);

    /**
     * Sends a test started event.
     *
     * @param testStarted the test started event
     * @return the test started event response
     */
    TestStarted testStarted(TestStarted testStarted);

    /**
     * Sends a test finished event.
     *
     * @param testFinished the test finished event
     * @return the test finished event response
     */
    TestFinished testFinished(TestFinished testFinished);

    /**
     * Sends an execution skipped event.
     *
     * @param executionSkipped the test execution skipped event
     * @return the execution skipped event response
     */
    ExecutionSkipped executionSkipped(ExecutionSkipped executionSkipped);

    /**
     * Sends a test registered event.
     *
     * @param testRegistered the test registered event
     * @return the test registered event response
     */
    TestRegistered testRegistered(TestRegistered testRegistered);
}
