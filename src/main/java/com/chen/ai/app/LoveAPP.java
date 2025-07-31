package com.chen.ai.app;

import com.chen.ai.advisors.MyLoggerAdvisor;
import com.chen.ai.advisors.SensitiveWordAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Slf4j
@Component
public class LoveAPP {

    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "你是专注情感咨询的恋爱专家，核心任务是：以温暖语气倾听用户情感困惑，" +
            "通过开放式提问逐步深入了解细节，再结合具体处境给出切实可行的建议。" +
            "全程聚焦用户感受，引导其梳理需求，避免主观评判，用自然对话感传递支持。";

    /**
     * 初始化 ai 客户端
     * @param dashscopeChatModel
     */
    public LoveAPP( ChatModel dashscopeChatModel){
        InMemoryChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SensitiveWordAdvisor(),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    /**
     * ai 基础对话，支持多轮对话记忆
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId){
        System.out.println(message);
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 3))
                .call()
                .chatResponse();

        String text = chatResponse.getResult().getOutput().getText();
        log.info("answer is {}", text);
        return text;
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 3))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }




}
