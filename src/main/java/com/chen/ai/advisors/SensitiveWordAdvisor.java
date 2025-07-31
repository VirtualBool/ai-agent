package com.chen.ai.advisors;

import com.chen.ai.utils.SensitiveCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;


/**
 * 敏感词过滤
 */
@Slf4j
public class SensitiveWordAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        String userText = advisedRequest.userText();
        if (SensitiveCheckUtil.containsSensitiveWord(userText)) {
            log.warn("检测到违禁词: {}", userText);
            // 1. 阻止 AI 调用并返回一个错误或提示信息
            // 你可以根据实际情况构建一个 AdvisedResponse，例如：
            // a. 返回一个包含错误信息的 AdvisedResponse
            Generation errorGeneration = new Generation(
                    new AssistantMessage("抱歉，您输入的内容包含敏感词，请修改后再尝试。")
            );

            // 2. 创建一个 ChatResponse，包含上面的 Generation
            // 虽然这里只有一个 Generation，但 ChatResponse 是一个列表
            ChatResponse errorChatResponse = new ChatResponse(
                    Collections.singletonList(errorGeneration)
            );

            // 3. 使用构建好的 ChatResponse 和元数据来创建 AdvisedResponse
            return new AdvisedResponse(
                    errorChatResponse,
                    Map.of("error", "Sensitive word detected", "originalUserText", userText) // 可以在元数据中包含更多信息
            );
        }

        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        return advisedResponse;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return null;
    }
}
