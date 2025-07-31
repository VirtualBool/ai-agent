package com.chen.ai.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.CommandLineRunner;


// 取消注释即可在 SpringBoot 项目启动时执行
//@Component
public class OllamaAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel ollamaChatModel;

    @Override
    public void run(String... args) throws Exception {

        ChatClient.builder(ollamaChatModel)
                .defaultSystem("你是一名恋爱咨询专家，专门回答用户恋爱中遇到的问题")
                .defaultAdvisors()
                .build();
    }
}
