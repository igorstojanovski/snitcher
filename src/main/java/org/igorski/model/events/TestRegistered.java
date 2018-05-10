package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Event that registers a test. This only registers the test to the event reporting facility that the test is part
 * of the test session and that other events for the test with this test ID will be sent as well.
 */
@Getter
@Setter
@NoArgsConstructor
public class TestRegistered extends Event {
    private String testId;
    private Long sessionId;

    public TestRegistered(long time, String testId, Long sessionId) {
        this.testId = testId;
        this.time = time;
        this.sessionId = sessionId;
    }
}
