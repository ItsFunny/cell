package com.cell.base.common.utils;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportUtil
{
    /**
     * 生成表格数据
     * enum中的值 要与data 中的数据一致
     * 一个enum 对应一种导出
     *
     * @param data
     * @return
     */
    public static XSSFWorkbook createTransactionRecordExcel(String sheetName, List data, Class<? extends Enum> anEnumClass) throws Exception
    {
        // 创建Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作簿
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表头样式
        XSSFCellStyle headStyle = setHeadStyle(workbook);
        // 设置表头
        XSSFRow header = sheet.createRow(0);
        Method method = anEnumClass.getDeclaredMethod("values");
        Enum[] values = (Enum[]) method.invoke(null);
        for (Enum val : values)
        {
            int ordinal = val.ordinal();
            XSSFCell headerCell = header.createCell(ordinal);
            Field fieldDes = anEnumClass.getDeclaredField("fieldDes");
            fieldDes.setAccessible(true);
            Object obj = fieldDes.get(val);
            headerCell.setCellValue(obj.toString());
            headerCell.setCellStyle(headStyle);
        }
        if (!CollectionUtils.isEmpty(data))
        {
            for (int j = 0; j < data.size(); j++)
            {
                XSSFRow row = sheet.createRow(j + 1);
                Object record = data.get(j);
                Class<?> aClass = record.getClass();
                for (Enum val : values)
                {
                    int ordinal = val.ordinal();
                    XSSFCell cell = row.createCell(ordinal);
                    Field fieldName = anEnumClass.getDeclaredField("fieldName");
                    fieldName.setAccessible(true);
                    Object o = fieldName.get(val);
                    Field declaredField = aClass.getDeclaredField(o.toString());
                    declaredField.setAccessible(true);
                    Object fieldVal = declaredField.get(data.get(j));
                    Method handle = anEnumClass.getDeclaredMethod("handle", Object.class);
                    Object invoke;
                    if (fieldVal == null)
                    {
                        invoke = "";
                    } else
                    {
                        invoke = handle.invoke(val, fieldVal);
                    }
                    cell.setCellValue(String.valueOf(invoke));
                }
            }
        }
        // 自适应宽度(中文支持)
        setSizeColumn(sheet, 10);
        return workbook;
    }

    /**
     * 设置表头样式
     *
     * @param wb
     * @return
     */
    public static XSSFCellStyle setHeadStyle(XSSFWorkbook wb)
    {
        XSSFCellStyle headStyle = wb.createCellStyle();
        // 设置背景颜色白色
        headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        // 设置填充颜色
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置上下左右边框
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setBorderTop(BorderStyle.THIN);
        // 设置水平居中
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置标题字体
        XSSFFont headFont = wb.createFont();
        // 设置字体大小
        headFont.setFontHeightInPoints((short) 12);
        // 设置字体
        headFont.setFontName("宋体");
        // 设置字体粗体
        headFont.setBold(true);
        // 把字体应用到当前的样式
        headStyle.setFont(headFont);
        return headStyle;
    }

    /**
     * 自适应宽度(中文支持)
     *
     * @param sheet
     * @param size
     */
    public static void setSizeColumn(XSSFSheet sheet, int size)
    {
        for (int columnNum = 0; columnNum < size; columnNum++)
        {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
            {
                XSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null)
                {
                    currentRow = sheet.createRow(rowNum);
                } else
                {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(columnNum) != null)
                {
                    XSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING)
                    {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length)
                        {
                            columnWidth = length;
                        }
                    }
                }
            }
            //sheet.setColumnWidth(columnNum, (columnWidth + 1) * 256);
            if (columnWidth > 255)
            {
                columnWidth = 255;
            }
            sheet.setColumnWidth(columnNum, (columnWidth) * 256);
        }
    }


    public static Map<String, List<String[]>> parseExcelToMap(String filePath, String sheetName) throws Exception
    {
        FileInputStream in=new FileInputStream(filePath);
        Map<String, List<String[]>> map = new HashMap<>();
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++)
        {
            XSSFSheet sheetAt = workbook.getSheetAt(i);

            if (sheetAt.getSheetName().equals("填写说明"))
            {
                continue;
            }
            List<String[]> table = new ArrayList<>();
            if (sheetName == null || sheetName.equals(sheetAt.getSheetName()))
            {
                for (int j = 0; j < sheetAt.getPhysicalNumberOfRows(); j++)
                {
                    XSSFRow row = sheetAt.getRow(j);
                    if (row == null)
                    {
                        continue;
                    }
                    List<String> s = new ArrayList<>();
                    for (int c = 0; c < row.getLastCellNum(); c++)
                    {
                        XSSFCell cell = row.getCell(c);
                        if (cell != null)
                        {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            s.add(row.getCell(c).getStringCellValue());
                        } else
                        {
                            s.add("");
                        }
                    }
                    String[] res = list2StrArray(s);
                    table.add(res);
                }
            }
            map.put(sheetAt.getSheetName(), table);
        }
        return map;
    }

    private static String[] list2StrArray(List<String> s)
    {
        String[] res = new String[s.size()];
        s.toArray(res);
        return res;
    }
}
