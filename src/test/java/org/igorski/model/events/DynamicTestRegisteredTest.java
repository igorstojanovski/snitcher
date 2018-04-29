package org.igorski.model.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import extensions.WiremockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(WiremockExtension.class)
class DynamicTestRegisteredTest {

    @Test
    public void shouldTest(WireMockServer wireMockServer) throws JsonProcessingException {
        System.out.println(wireMockServer);
        ObjectMapper objectMapper = new ObjectMapper();
        SessionStarted sessionStarted = new SessionStarted();
        sessionStarted.setId(1);
        sessionStarted.setProjectName("project");
        sessionStarted.setSessionId(1234);
        sessionStarted.setTime(System.currentTimeMillis());
        sessionStarted.setUser("user");

        objectMapper.writerWithDefaultPrettyPrinter();
        System.out.println(objectMapper.writeValueAsString(sessionStarted));
    }

}