package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.igorski.SnitcherProperties;

/**
 * Event that marks a session as started. This object contains the session ID.
 */
@Getter
@Setter
@NoArgsConstructor
public class SessionStarted extends Event {
    protected String projectName = SnitcherProperties.getValue("project.name");
    protected String user = SnitcherProperties.getValue("snitching.user");
    private long sessionId;

    public SessionStarted(long time) {
        this.time = time;
    }
}
