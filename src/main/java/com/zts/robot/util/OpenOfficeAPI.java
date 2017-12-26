package com.zts.robot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 主要用openOffice 的API向加密的word文件中写入文字和在图片;
 * 
 * 
 */
public class OpenOfficeAPI {
   
    public static void main(String[] args) throws Exception {
        // 模板文件路径
        String templatePath = "D:/ROOT/staticrobot/badge/陈野胸卡.pdf";
        // 生成的文件路径
        String targetPath = "D:/ROOT/staticrobot/badge/陈野胸卡1.pdf";
        // 书签名
        String fieldName = "field";
        // 图片路径
        String imagePath = "D:/ROOT/staticrobot/badge/11.jpg";

        // 读取模板文件
        InputStream input = new FileInputStream(new File(templatePath));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(templatePath));
        // 提取pdf中的表单
        /*AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));

        // 通过域名获取所在页和坐标，左下角为起点
        int pageNo = form.getFieldPositions(fieldName).get(0).page;
        com.itextpdf.text.Rectangle signRect = form.getFieldPositions(fieldName).get(0).position;
        float x = signRect.getLeft();
        float y = signRect.getBottom();
        */
      
        // 读图片
        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imagePath);
        // 获取操作的页面
        //PdfContentByte under = stamper.getOverContent(pageNo);
        PdfContentByte under = stamper.getOverContent(1);
        // 根据域的大小缩放图片
        image.scaleToFit(170, 170);
        //image.scaleToFit(signRect.getWidth(), signRect.getHeight());
        // 添加图片
        //image.setAbsolutePosition(x, y);
        image.setAbsolutePosition(245, 350);
        under.addImage(image);

        stamper.close();
        reader.close();
    }

public static void pdfaddpicurl(String templatePath, String imagePath) throws IOException, DocumentException{
	 // 模板文件路径
    // templatePath = "D:/ROOT/staticrobot/badge/陈野胸卡.pdf";
    // 图片路径
    // imagePath = "D:/ROOT/staticrobot/badge/11.jpg";
     // 读取模板文件
     InputStream input = new FileInputStream(new File(templatePath));
     PdfReader reader = new PdfReader(input);
     PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(templatePath));
     

     // 读图片
     com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imagePath);
     // 获取操作的页面
     //PdfContentByte under = stamper.getOverContent(pageNo);
     PdfContentByte under = stamper.getOverContent(1);
     // 根据域的大小缩放图片
     image.scaleToFit(170, 170);
     //image.scaleToFit(signRect.getWidth(), signRect.getHeight());
     // 添加图片
     //image.setAbsolutePosition(x, y);
     image.setAbsolutePosition(245, 350);
     under.addImage(image);

     stamper.close();
     reader.close();
}


}