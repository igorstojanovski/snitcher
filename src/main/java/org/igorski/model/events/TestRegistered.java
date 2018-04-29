package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
