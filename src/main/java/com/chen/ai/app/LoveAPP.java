package com.chen.ai.app;

import com.chen.ai.advisors.MyLoggerAdvisor;
import com.chen.ai.advisors.SensitiveWordAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Slf4j
@Component
public class LoveAPP {

    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "你是一个智能助手，需要满足：准确回应，友好适配，拒违规，不知明说，专注解决问题";
    @Resource
    private VectorStore loveAppVectorStore;
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
       // log.info("answer is {}", text);
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
       // log.info("loveReport: {}", loveReport);
        return loveReport;
    }




    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
       // log.info("content: {}", content);
        return content;
    }


    @Resource
    private Advisor loveAppRagCloudAdvisor;

    public String doChatWithRagCloud(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用增强检索服务（云知识库服务）
                .advisors(loveAppRagCloudAdvisor)

                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
       // log.info("content: {}", content);
        return content;
    }

    @Resource
    ToolCallback[] allTools;
    public String doChatWithTools(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();

        return content;
    }


}
