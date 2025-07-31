package com.chen.ai.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SensitiveCheckUtil {



    private static Pattern pattern;

    private static List<String> words;
    static{
        Resource sensitiveWordsResource = new ClassPathResource("static/SensitiveWords.json") ;

        ObjectMapper mapper = new ObjectMapper();
        //如果JSON根节点是数组（修改后的JSON格式）
        try {
            words = mapper.readValue(
                    sensitiveWordsResource.getInputStream(),
                    new TypeReference<List<String>>() {}
            );
            log.info("成功加载" + words.size() + "个敏感词");
        } catch (IOException e) {
            log.error("敏感词文件加载错误");
            e.printStackTrace();
        }

        //构建正则表达式
        String regex = words.stream()
                .map(Pattern::quote) // 转义特殊字符
                .reduce((w1, w2) -> w1 + "|" + w2)
                .orElse("");

        pattern = Pattern.compile("(?i)" + regex); // 忽略大小写
    }


    // 检查是否包含敏感词
    public static boolean containsSensitiveWord(String content) {
        return pattern.matcher(content).find();
    }

    // 过滤敏感词
    public static String filterContent(String content) {

        return pattern.matcher(content)
                .replaceAll(match -> "*".repeat(match.group().length()));
    }
}
