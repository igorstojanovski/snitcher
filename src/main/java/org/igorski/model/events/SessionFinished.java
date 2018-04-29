package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionFinished extends Event {
    private long sessionId;

    public SessionFinished(long time, long sessionId) {
        this.time = time;
        this.sessionId = sessionId;
    }
}
