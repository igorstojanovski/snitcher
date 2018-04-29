package org.igorski.model.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Event {
    protected long id;
    protected long time;
    protected String projectName;
    protected String user;
}
