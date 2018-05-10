package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Basic event that all other events inherit from. It consists of event ID and time.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Event {
    protected long id;
    protected long time;
}
