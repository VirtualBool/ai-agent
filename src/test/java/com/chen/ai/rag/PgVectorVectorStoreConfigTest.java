package com.chen.ai.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class PgVectorVectorStoreConfigTest {

    @Resource
    VectorStore pgVectorVectorStore;

    @Test
    void test() {
        List<Document> documents = List.of(
                new Document("沈阳工业大学是一所位于沈阳的工业类大学，最好的学科是电气自动化", Map.of("meta1", "meta1")),
                new Document("沈阳工业大学研发薪资很少"),
                new Document("沈阳工业大学没有空调", Map.of("meta2", "meta2")));
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("上沈阳工业大学应该选择哪个专业？").topK(3).build());
        Assertions.assertNotNull(results);
    }
}
