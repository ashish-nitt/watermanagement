package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaterManagementAppTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/testresults")
    void test(String inpFileName, String outputFileName) throws IOException {
        WaterManagementApp app = new WaterManagementApp();
        String outputValue = new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(outputFileName)).readAllBytes());
        assertEquals(outputValue, app.process(inpFileName));
    }
}