package org.igorski.listener;

import org.igorski.communication.client.CentralCommitteeProxy;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.HashSet;
import java.util.Set;

public class Snitcher implements TestExecutionListener {


    private final CentralCommitteeProxy centralCommitteeProxy;

    public Snitcher() {
        this(new CentralCommitteeProxy());
    }

    Snitcher(CentralCommitteeProxy centralCommitteeProxy) {
        this.centralCommitteeProxy = centralCommitteeProxy;
    }

    public void testPlanExecutionStarted(TestPlan testPlan) {
        centralCommitteeProxy.testPlanExecutionStarted(getUniqueIds(testPlan));
    }

    public void testPlanExecutionFinished(TestPlan testPlan) {
        centralCommitteeProxy.testPlanExecutionFinished();
    }

    public void dynamicTestRegistered(TestIdentifier testIdentifier) {

    }

    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        centralCommitteeProxy.executionSkipped(testIdentifier.getUniqueId(), reason);
    }

    public void executionStarted(TestIdentifier testIdentifier) {
        centralCommitteeProxy.executionStarted(testIdentifier.getUniqueId());
    }

    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        centralCommitteeProxy.executionFinished(testExecutionResult.getStatus(), testIdentifier.getUniqueId());
    }

    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {

    }

    public Set<String> getUniqueIds(TestPlan testPlan) {
        Set<TestIdentifier> roots = testPlan.getRoots();
        Set<String> uniqueIds = new HashSet<String>();

        for(TestIdentifier testIdentifier : roots) {
            Set<TestIdentifier> children = testPlan.getChildren(testIdentifier.getUniqueId());
            registerChildren(children, testPlan, uniqueIds);
        }

        return uniqueIds;
    }

    private void registerChildren(Set<TestIdentifier> children, TestPlan testPlan, Set<String> uniqueIds) {
        for(TestIdentifier child : children) {
            if(child.isTest()) {
                uniqueIds.add(child.getUniqueId());
            } else {
                registerChildren(testPlan.getChildren(child.getUniqueId()), testPlan, uniqueIds);
            }
        }
    }
}
