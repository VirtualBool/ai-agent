package com.chen.ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class WeatherTool {

    @Tool(description = "get the weather by city name ")
    public String getWeatherByCity(@ToolParam(description = "the city name") String city){

        return "今天雨，小心防晒";
    }
}
