package org.igorski.communication.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import extensions.WiremockExtension;
import org.igorski.model.events.SessionStarted;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@ExtendWith(WiremockExtension.class)
class EventDispatcherIT {

    private EventDispatcher eventDispatcher;

    @BeforeEach
    public void beforeEachTest() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        client.register(new ClientRequestFilter() {
            @Override
            public void filter(ClientRequestContext clientRequestContext) throws IOException {

                System.out.println(clientRequestContext.getUri());
            }
        });
        ResteasyWebTarget webTarget = client.target(UriBuilder.fromPath("http://localhost:8080"));
        eventDispatcher = webTarget.proxy(EventDispatcher.class);
    }

    @Test
    public void shouldSendRequest(WireMockServer wireMockServer) {
        wireMockServer.
        stubFor(post(urlEqualTo("/api/events/session/started"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}"))
        );

        System.out.println(wireMockServer.getStubMappings().get(0));

        SessionStarted sessionStarted = new SessionStarted();
        SessionStarted sessionStartedREsponse = eventDispatcher.sessionStarted(sessionStarted);
        System.out.println(sessionStartedREsponse.getSessionId());

    }
}