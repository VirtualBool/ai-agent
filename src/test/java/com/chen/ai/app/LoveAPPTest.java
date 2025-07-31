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
}