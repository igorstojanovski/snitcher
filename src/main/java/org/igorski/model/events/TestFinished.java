package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
