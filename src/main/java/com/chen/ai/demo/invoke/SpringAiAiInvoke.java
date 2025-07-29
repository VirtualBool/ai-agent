package com.chen.ai.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

// 取消注释即可在 SpringBoot 项目启动时执行

public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {

        AssistantMessage output = dashscopeChatModel.call(new Prompt("你是谁 ?"))
                .getResult()
                .getOutput();

        System.out.println(output);
    }
}
