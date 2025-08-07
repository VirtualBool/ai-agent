package com.chen.ai.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherToolTest {

    @Test
    void getWeatherByCity() {
        WeatherTool weatherTool = new WeatherTool();
        String zz = weatherTool.getWeatherByCity("郑州");
        Assertions.assertNotNull(zz);
    }
}