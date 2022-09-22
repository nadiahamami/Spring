package com.vermeg.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.vermeg.payload.responses.IssueDetails;
import org.json.JSONArray;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.springframework.util.ResourceUtils;


public class Utilities {


    public  static Stream<JsonElement> arrayToStream(JsonArray array) {
        return      StreamSupport.stream( Spliterators.spliteratorUnknownSize(array.iterator(),
                Spliterator.ORDERED),false);
    }

    public  static LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date  = date.split("T")[0];
        return   LocalDate.parse(date , formatter );
    }
    public static ByteArrayInputStream getExcel(List<IssueDetails> issueDetails , String title, String file ) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        FileInputStream inputStream = new FileInputStream(ResourceUtils.getFile(file)) ;
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        IssueDetails[] bookData = new IssueDetails[issueDetails.size()] ;
        issueDetails.toArray(bookData) ;


        int rowCount = sheet.getFirstRowNum()+1;
        Row rowTitle = sheet.createRow(0);
        Cell cellTitle= rowTitle.createCell(0);
        CellStyle cellStyleTitle= workbook.createCellStyle();
        cellStyleTitle.setWrapText(true);
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellTitle.setCellStyle(cellStyleTitle);
        cellTitle.setCellValue(title);


        for (IssueDetails field : bookData) {
            Row  row = sheet.createRow(++rowCount);
            int  columnCount = 0;
            for(String obj : field.getString().split(",")){
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(obj);
            }



        }
        inputStream.close();
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

}
