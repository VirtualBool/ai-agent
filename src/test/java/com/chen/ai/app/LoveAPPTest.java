package com.chen.ai.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoveAPPTest {

    @Resource
    private LoveAPP loveAPP;

    @Test
    void testChat() {
        String id = UUID.randomUUID().toString();
        String mes = "我想要自杀";
        loveAPP.doChat(mes, id);
    }

    @Test
    void doChatWithRag() {
        String id = UUID.randomUUID().toString();
        String mes = "我想要脱单应该怎么做？";
        loveAPP.doChatWithRag(mes, id);
    }


    @Test
    void doChatWithRagCloud() {
        String id = UUID.randomUUID().toString();
        String mes = "我想要脱单应该怎么做？";
        loveAPP.doChatWithRagCloud(mes, id);
    }
}