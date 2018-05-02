package org.igorski.communication.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import extensions.WiremockExtension;
import org.igorski.model.events.SessionFinished;
import org.igorski.model.events.SessionStarted;
import org.igorski.model.events.TestStarted;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(WiremockExtension.class)
class EventDispatcherIT {
    public static final String USER = "igor";
    public static final String PROJECT_NAME = "TestingProject";
    public static final long SESSION_ID = 12345L;
    public static final long ID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private EventDispatcher eventDispatcher;

    @BeforeEach
    public void beforeEachTest() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        client.register((ClientRequestFilter) clientRequestContext -> System.out.println(clientRequestContext.getUri()));
        ResteasyWebTarget webTarget = client.target(UriBuilder.fromPath("http://localhost:8080"));
        eventDispatcher = webTarget.proxy(EventDispatcher.class);
    }

    @Test
    public void shouldSendSessionCreateRequest(WireMockServer wireMockServer) throws JsonProcessingException {
        SessionStarted sessionStartedExpectedResponse = new SessionStarted();
        sessionStartedExpectedResponse.setUser(USER);
        sessionStartedExpectedResponse.setProjectName(PROJECT_NAME);
        sessionStartedExpectedResponse.setSessionId(SESSION_ID);
        sessionStartedExpectedResponse.setId(ID);

        SessionStarted sessionStartedRequest = new SessionStarted();
        sessionStartedRequest.setUser(USER);
        sessionStartedRequest.setProjectName(PROJECT_NAME);

        wireMockServer.stubFor(post(urlEqualTo("/api/events/session/started"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(sessionStartedRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(sessionStartedExpectedResponse)))
        );

        SessionStarted sessionStartedResponse = eventDispatcher.sessionStarted(sessionStartedRequest);
        assertThat(sessionStartedResponse.getSessionId()).isEqualTo(SESSION_ID);
        assertThat(sessionStartedResponse.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldSendSessionEndedRequest(WireMockServer wireMockServer) throws JsonProcessingException {
        SessionFinished sessionFinishedExpectedResponse = new SessionFinished();
        sessionFinishedExpectedResponse.setSessionId(SESSION_ID);
        sessionFinishedExpectedResponse.setId(ID);

        SessionFinished sessionFinishedRequest = new SessionFinished();
        sessionFinishedRequest.setSessionId(SESSION_ID);
        sessionFinishedRequest.setId(ID);

        wireMockServer.stubFor(post(urlEqualTo("/api/events/session/finished"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(sessionFinishedRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(sessionFinishedExpectedResponse)))
        );

        SessionFinished sessionFinishedResponse = eventDispatcher.sessionFinished(sessionFinishedRequest);
        assertThat(sessionFinishedResponse.getSessionId()).isEqualTo(SESSION_ID);
        assertThat(sessionFinishedResponse.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldSendTestStartedRequest(WireMockServer wireMockServer) throws JsonProcessingException {
        TestStarted testStartedExpectedResponse = new TestStarted();
        testStartedExpectedResponse.setTestId("test_id_123");
        testStartedExpectedResponse.setSessionId(SESSION_ID);
        testStartedExpectedResponse.setId(ID);

        TestStarted testStartedRequest = new TestStarted();
        testStartedRequest.setSessionId(SESSION_ID);
        testStartedExpectedResponse.setTestId("test_id_123");

        wireMockServer.stubFor(post(urlEqualTo("/api/events/test/started"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(testStartedRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(testStartedExpectedResponse)))
        );

        TestStarted testStartedResponse = eventDispatcher.testStarted(testStartedRequest);
        assertThat(testStartedResponse.getSessionId()).isEqualTo(SESSION_ID);
        assertThat(testStartedResponse.getId()).isEqualTo(1L);
    }
}