package extensions;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WiremockExtension implements BeforeAllCallback, AfterEachCallback, AfterAllCallback, ParameterResolver {

    private WireMockServer wireMockServer;

    public void afterAll(ExtensionContext extensionContext) {
        wireMockServer.stop();
    }

    public void afterEach(ExtensionContext extensionContext) {
        wireMockServer.start();
    }

    public void beforeAll(ExtensionContext extensionContext) {
        wireMockServer = new WireMockServer(wireMockConfig().port(8282));
        wireMockServer.start();
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return WireMockServer.class.equals(parameterContext.getParameter().getType());
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(WireMockServer.class.equals(parameterContext.getParameter().getType())) {
            return wireMockServer;
        }
        return null;
    }
}
