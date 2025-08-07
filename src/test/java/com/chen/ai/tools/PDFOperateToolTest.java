package com.chen.ai.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFOperateToolTest {

    @Test
    void getTextFromPDF() {
        PDFOperateTool pdfOperateTool = new PDFOperateTool();
        String textFromPDF = pdfOperateTool.getTextFromPDF("vitae.pdf");
        Assertions.assertNotNull(textFromPDF);

    }
}