package com.chen.ai.tools;


import com.chen.ai.constant.FileConstant;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;
import java.io.IOException;

public class PDFOperateTool {
    @Tool(description = " get all text from a PDF")
    public String getTextFromPDF(@ToolParam(description = "filename") String filename) {

        String FILE_DIR = FileConstant.FILE_SAVE_DIR + filename;
        String result = extractTextFromPDF(FILE_DIR);
        return result;
    }

    /**
     * 从 PDF 文件中提取文本内容
     * @param pdfFilePath PDF 文件的路径
     * @return 提取的文本字符串，如果发生错误则返回 null
     */
    public static String extractTextFromPDF(String pdfFilePath) {
        PDDocument document = null;
        try {
            // 加载 PDF 文档
            document = PDDocument.load(new File(pdfFilePath));

            // 检查文档是否被加密
            if (document.isEncrypted()) {
                System.err.println("PDF 文档已加密，无法提取文本");
                return null;
            }
            // 创建 PDFTextStripper 对象用于提取文本
            PDFTextStripper stripper = new PDFTextStripper();

            // 可以设置提取的页码范围，默认提取所有页
            // stripper.setStartPage(1);
            // stripper.setEndPage(3);

            // 提取文本
            return stripper.getText(document);

        } catch (IOException e) {
            System.err.println("提取文本时发生错误：" + e.getMessage());
            return null;
        } finally {
            // 关闭文档，释放资源
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
