package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Event that marks a session as finished.
 */
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
