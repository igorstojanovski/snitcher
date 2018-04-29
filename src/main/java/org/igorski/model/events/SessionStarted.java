package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionStarted extends Event {
    private long sessionId;

    public SessionStarted(long time) {
        this.time = time;
    }
}
