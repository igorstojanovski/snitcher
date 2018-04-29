package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExecutionSkipped extends Event {
    private String testId;
    private String reason;
    private long sessionId;

    public ExecutionSkipped(long time, String testId, long sessionId, String reason) {
        this.time = time;
        this.testId = testId;
        this.sessionId = sessionId;
        this.reason = reason;
    }
}
