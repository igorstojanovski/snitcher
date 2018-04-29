package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestStarted extends Event {
    private String testId;
    private long sessionId;

    public TestStarted(long time, String testId, long sessionId) {
        this.time = time;
        this.testId = testId;
        this.sessionId = sessionId;
    }
}
