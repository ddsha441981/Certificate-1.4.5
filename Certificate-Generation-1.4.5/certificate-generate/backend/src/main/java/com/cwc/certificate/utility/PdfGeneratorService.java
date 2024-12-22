package com.cwc.certificate.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cwc.certificate.config.ConstantValue;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Service
@Slf4j
public class PdfGeneratorService {

    private static Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD,new BaseColor(52, 73, 94));
    private static Font COURIER = new Font(Font.FontFamily.COURIER, 18, Font.BOLD,new BaseColor(52, 73, 94));


    public byte[] generatePdf(List<Map<String, String>> data, String gateway) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        //empty PDF open
        document.open();

        if (gateway != null && !gateway.isEmpty()) {
            gateway = gateway.substring(0, 1).toUpperCase() + gateway.substring(1);
            Paragraph title = new Paragraph(gateway + ConstantValue.DEFAULT_PDF_REPORT_NAME, COURIER);
            leaveEmptyLine(title, 1);
            title.setAlignment(Element.ALIGN_CENTER);
            // Add spacing after the title
            title.setSpacingAfter(20);
            document.add(title);

            // table with dynamic columns
            PdfPTable table = new PdfPTable(data.get(0).size());
            table.setWidthPercentage(100);


            // header font and style
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            headerFont.setColor(BaseColor.WHITE); // Set text color
            BaseColor headerBackgroundColor = new BaseColor(52, 73, 94); // Customize background color

            //column headers
            for (String columnName : data.get(0).keySet()) {
                PdfPCell cell = new PdfPCell(new Phrase(columnName,headerFont));
                cell.setBackgroundColor(headerBackgroundColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8); // padding
                table.addCell(cell);
            }

            // Add data rows
            for (Map<String, String> row : data) {
                for (String columnName : row.keySet()) {
                    table.addCell(row.get(columnName));
                }
            }
            //add logo here
           // addLogo(document);

            //add data
            document.add(table);
            //add footer
            addFooter(document,gateway);
            document.close();
        }

        return baos.toByteArray();
    }

    private void addLogo(Document document) {
        try {
            Image logo = Image.getInstance("classpath:assets/logo.png");

            logo.scaleToFit(50, 50);
            logo.setAbsolutePosition(50, 700);
            logo.setAlignment(Element.ALIGN_LEFT);
            logo.setAbsolutePosition(document.leftMargin(), document.top() - logo.getScaledHeight());
            document.add(logo);
        } catch (DocumentException | IOException e) {
            log.error("Document exception occurs {} " , e.getMessage());
        }
    }

    private void addFooter(Document document,String filename) throws DocumentException {
        Paragraph p2 = new Paragraph();
        leaveEmptyLine(p2, 3);
        p2.setAlignment(Element.ALIGN_CENTER);
       // p2.add(new Paragraph("----End Of " +filename+"-Report----",COURIER_SMALL_FOOTER));
        document.add(p2);
    }

    private static void leaveEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}

