package org.igorski;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnitcherPropertiesTest {

    @Test
    public void shouldGetProperty() {
        System.out.println(SnitcherProperties.getValue("snitching.user"));
    }
}