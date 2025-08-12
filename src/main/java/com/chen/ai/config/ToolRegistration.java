package com.chen.ai.config;

import com.chen.ai.tools.PDFOperateTool;
import com.chen.ai.tools.TerminateTool;
import com.chen.ai.tools.WeatherTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegistration {



    @Bean
    public ToolCallback[] allTools() {
        PDFOperateTool pdfOperateTool = new PDFOperateTool();
        WeatherTool weatherTool = new WeatherTool();
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
             terminateTool,
             pdfOperateTool,
             weatherTool
        );
    }
}
