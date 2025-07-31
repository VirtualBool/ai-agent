package com.chen.ai.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class SensitiveCheckUtilTest {

    @Test
    void containsSensitiveWord() {
        if(SensitiveCheckUtil.containsSensitiveWord("自杀")){
            log.info("包含敏感词");
        }
    }

    @Test
    void filterContent() {
        String content = SensitiveCheckUtil.filterContent("我想自杀");
        Assertions.assertNotNull(content);
    }
}