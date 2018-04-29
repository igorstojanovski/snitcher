package org.igorski.communication.client;

import org.igorski.model.events.SessionStarted;
import org.igorski.model.events.TestRegistered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private SessionStarted sessionStarted;
    @Captor
    private ArgumentCaptor<TestRegistered> testRegisteredCaptor;

    private CentralCommitteeProxy centralCommitteeProxy;

    @BeforeEach
    public void beforeEachTest() {
        centralCommitteeProxy = new CentralCommitteeProxy(eventDispatcher);
    }

    @Test
    public void shouldDispatchTestPlanStartedEvents() {
        when(sessionStarted.getSessionId()).thenReturn(SESSION_ID);
        when(eventDispatcher.sessionStarted(any(SessionStarted.class))).thenReturn(sessionStarted);

        HashSet<String> uniqueIds = new HashSet<>();
        uniqueIds.add("id1");
        uniqueIds.add("id2");

        centralCommitteeProxy.testPlanExecutionStarted(uniqueIds);
        verify(eventDispatcher, times(2)).testRegistered(testRegisteredCaptor.capture());

        List<TestRegistered> testRegisteredEvent = testRegisteredCaptor.getAllValues();
        assertThat(testRegisteredEvent.stream().map(TestRegistered::getTestId).collect(Collectors.toList()))
                .containsAll(Arrays.asList("id1", "id2"));
    }
}