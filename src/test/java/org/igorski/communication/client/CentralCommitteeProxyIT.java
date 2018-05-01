package org.igorski.communication.client;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

class CentralCommitteeProxyIT {


    @Test
    public void shouldStartSession() {
        CentralCommitteeProxy centralCommitteeProxy = new CentralCommitteeProxy();
        HashSet<String> uniqueIds = new HashSet<>();
        uniqueIds.add("id1");
        uniqueIds.add("id2");

        centralCommitteeProxy.testPlanExecutionStarted(uniqueIds);
    }

}