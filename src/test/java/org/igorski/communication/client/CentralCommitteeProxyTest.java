package org.igorski.communication.client;

import org.igorski.model.events.ExecutionSkipped;
import org.igorski.model.events.SessionFinished;
import org.igorski.model.events.SessionStarted;
import org.igorski.model.events.TestFinished;
import org.igorski.model.events.TestRegistered;
import org.igorski.model.events.TestStarted;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CentralCommitteeProxyTest {
    private static final Long SESSION_ID = 1234L;
    private static final String TEST_UNIQUE_ID = "uniqueId";
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private SessionStarted sessionStarted;
    @Captor
    private ArgumentCaptor<TestRegistered> testRegisteredCaptor;
    @Captor
    private ArgumentCaptor<SessionFinished> sessionFinishedArgumentCaptor;
    @Captor
    private ArgumentCaptor<TestStarted> testStartedArgumentCaptor;
    @Captor
    private ArgumentCaptor<TestFinished> testFinishedArgumentCaptor;
    @Captor
    private ArgumentCaptor<ExecutionSkipped> executionSkippedArgumentCaptor;
    @Captor
    private ArgumentCaptor<SessionStarted> sessionStartedArgumentCaptor;

    private CentralCommitteeProxy centralCommitteeProxy;

    @BeforeEach
    public void beforeEachTest() {
        when(sessionStarted.getSessionId()).thenReturn(SESSION_ID);
        when(eventDispatcher.sessionStarted(any(SessionStarted.class))).thenReturn(sessionStarted);
        centralCommitteeProxy = new CentralCommitteeProxy(eventDispatcher);
    }

    @Test
    public void shouldStartSession() {
        verify(eventDispatcher).sessionStarted(sessionStartedArgumentCaptor.capture());
        SessionStarted sessionStarted = sessionStartedArgumentCaptor.getValue();

        assertThat(sessionStarted.getUser()).isEqualTo("igor");
        assertThat(sessionStarted.getProjectName()).isEqualTo("SnitcherTesting");
    }

    @Test
    public void shouldDispatchTestPlanStartedEvents() {
        HashSet<String> uniqueIds = new HashSet<>();
        uniqueIds.add("id1");
        uniqueIds.add("id2");

        centralCommitteeProxy.testPlanExecutionStarted(uniqueIds);
        verify(eventDispatcher, times(2)).testRegistered(testRegisteredCaptor.capture());

        List<TestRegistered> testRegisteredEvent = testRegisteredCaptor.getAllValues();
        assertThat(testRegisteredEvent.stream().map(TestRegistered::getTestId).collect(Collectors.toList()))
                .containsAll(Arrays.asList("id1", "id2"));
    }

    @Test
    public void shouldDispatchSessionFinishedEvent() {
        centralCommitteeProxy.testPlanExecutionFinished();

        verify(eventDispatcher).sessionFinished(sessionFinishedArgumentCaptor.capture());
        assertThat(sessionFinishedArgumentCaptor.getValue().getSessionId()).isEqualTo(SESSION_ID);
    }

    @Test
    public void shouldDispatchExecutionStartedEvent() {
        centralCommitteeProxy.executionStarted(TEST_UNIQUE_ID);

        verify(eventDispatcher).testStarted(testStartedArgumentCaptor.capture());
        TestStarted testStartedEvent = testStartedArgumentCaptor.getValue();

        assertThat(testStartedEvent.getSessionId()).isEqualTo(SESSION_ID);
        assertThat(testStartedEvent.getTime()).isNotNull();
    }

    @Test
    public void shouldDispatchExecutionSkippedEvent() {
        String reason = "ignored";
        centralCommitteeProxy.executionSkipped(TEST_UNIQUE_ID, reason);

        verify(eventDispatcher).executionSkipped(executionSkippedArgumentCaptor.capture());
        ExecutionSkipped executionSkipped = executionSkippedArgumentCaptor.getValue();

        assertThat(executionSkipped.getSessionId()).isEqualTo(SESSION_ID);
        assertThat(executionSkipped.getTime()).isNotNull();
        assertThat(executionSkipped.getReason()).isEqualTo(reason);
    }

    @Test
    public void shouldDispatchExecutionFinishedEvent() {
        centralCommitteeProxy.executionFinished(TestExecutionResult.Status.SUCCESSFUL, TEST_UNIQUE_ID);

        verify(eventDispatcher).testFinished(testFinishedArgumentCaptor.capture());
        TestFinished testFinished = testFinishedArgumentCaptor.getValue();

        assertThat(testFinished.getSessionId()).isEqualTo(SESSION_ID);
        assertThat(testFinished.getTime()).isNotNull();
        assertThat(testFinished.getOutcome()).isEqualTo(TestExecutionResult.Status.SUCCESSFUL.toString());
    }
}