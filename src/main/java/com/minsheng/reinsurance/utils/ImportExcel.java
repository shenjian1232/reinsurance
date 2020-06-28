package com.minsheng.reinsurance.utils;

import com.minsheng.reinsurance.bean.entity.ExcelDataDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import static org.apache.poi.ss.usermodel.CellType.*;

/**
 *
 */
public class ImportExcel {
    public static List<ExcelDataDto> importData( MultipartFile file) throws Exception {

        //获取workbook对象
        Workbook workbook = null;
        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        //根据后缀名是否excel文件
        if (filename.endsWith("xls")) {
            //2003
            workbook = new HSSFWorkbook(inputStream);
        } else if (filename.endsWith("xlsx")) {
            //2007
            workbook = new XSSFWorkbook(inputStream);
        }
        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet = workbook.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<ExcelDataDto> excelDataDtoList = new ArrayList<ExcelDataDto>();
        ExcelDataDto excelDataDto;

        String br = "\n";
        Row rowHeader = sheet.getRow(0);

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                continue;
            }
            excelDataDto = new ExcelDataDto();
            //循环Excel的列
            //采用反射的方式判断每列数据并读取数据
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    Object  cellValue = getCellValue(cell);
                    Cell cellHeader = rowHeader.getCell(c);
                    System.out.println((r + 1) + "行" + (c + 1) + "列 ==cellHeader==" + cellHeader.getStringCellValue() + "==cellValue==" + cellValue);
                    if((c + 1)==1){
                        excelDataDto.setOfficeId(Integer.parseInt((String) cellValue) );
                    }if((c + 1)==2){
                        excelDataDto.setLoginName((String) cellValue);
                    }if((c + 1)==3){
                        excelDataDto.setName((String) cellValue);
                    }
                } else {
                    rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                }
            }
            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage)) {
                errorMsg += br + "第" + (r + 1) + "行，" + rowMessage;
                System.out.println(errorMsg);
                break;
            } else {
                excelDataDtoList.add(excelDataDto);
            }

        }
        inputStream.close();
        return excelDataDtoList;
    }


    public static Object getCellValue(Cell cell){
        Object cellValue = "";
        DecimalFormat df = new DecimalFormat("0");//格式化number String字符串
        if(cell == null){
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: //数字0
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    cellValue = df.format(cell.getNumericCellValue());
                }else{
                    cellValue = df.format(cell.getNumericCellValue());
                }
                break;
            case STRING: //字符串1
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                //cellValue = String.valueOf(cell.getCellFormula());
                try {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    cellValue = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    //判断row是否为空
    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != BLANK) {
                return false;
            }
        }
        return true;
    }


    //检查文件类型
    public static Boolean checkFile(MultipartFile file) throws Exception{
        //检查文件是否是excel类型文件
        String filename = file.getOriginalFilename();
        if (!filename.endsWith("xls") && !filename.endsWith("xlsx")) {
            return false;
        }
        //检查文件是否为空
        boolean empty = file.isEmpty();
        if (empty || file.getSize() == 0) {
            return false;
        }
        return true;

    }


}

