package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Event that marks a test as finished. It must contain the outcome of the test execution.
 */
@Getter
@Setter
@NoArgsConstructor
public class TestFinished extends Event {
    private long sessionId;
    private String outcome;
    private String testId;

    public TestFinished(long time, String testId, long sessionId, String outcome) {
        this.time = time;
        this.testId = testId;
        this.sessionId = sessionId;
        this.outcome = outcome;
    }
}
