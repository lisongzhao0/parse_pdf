package com.happy.gene.utility;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 14/06/2017.
 */
public class OfficeFileUtil {

    public static OfficeFileUtil newInstance() {
        return new OfficeFileUtil();
    }

    private FileUtil   fileUtil   = FileUtil.newInstance();
    private StringUtil stringUtil = StringUtil.newInstance();
    private ColorUtil  colorUtil  = ColorUtil.newInstance();
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public Workbook getExcel(String filePath) {
        InputStream is = fileUtil.getFileInputStream(filePath);
        if (null == is) {
            return null;
        }

        String ext = filePath.substring(filePath.lastIndexOf("."));
        Workbook wb = null;
        try {
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (IOException e) {
            System.err.println("error : " + e.getMessage());
            wb = null;
        }
        return wb;
    }

    public int getExcelSheetSize(Workbook workbook) {
        return null==workbook ? 0 : workbook.getNumberOfSheets();
    }

    public Sheet getExcelSheet(Workbook workbook, int sheetIndex) {
        if (null == workbook) {
            return null;
        }
        return workbook.getSheetAt(sheetIndex);
    }

    public int getFirstRowNumber(Sheet sheet) {
        return sheet.getFirstRowNum();
    }

    public int getLastRowNumber(Sheet sheet) {
        return sheet.getLastRowNum();
    }

    public Row getExcelRow(Sheet sheet, int rowIndex) {
        if (null == sheet) {
            return null;
        }

        return sheet.getRow(rowIndex);
    }

    public int getExcelRowCellNumber(Sheet sheet, int rowIndex) {
        if (null == sheet) {
            return 0;
        }

        Row row = sheet.getRow(rowIndex);
        int colNum = row.getPhysicalNumberOfCells();

        return colNum;
    }

    public int getExcelRowCellNumber(Row row) {
        if (null == row) {
            return 0;
        }

        int colNum = row.getPhysicalNumberOfCells();

        return colNum;
    }

    public int getExcelRowCellStart(Sheet sheet, int rowIndex) {
        if (null == row) {
            return 0;
        }

        Row row = sheet.getRow(rowIndex);
        int colStart = row.getFirstCellNum();

        return colStart;
    }

    public int getExcelRowCellStart(Row row) {
        if (null == row) {
            return 0;
        }

        int colStart = row.getFirstCellNum();

        return colStart;
    }

    public Object getExcelRowCellValue(Sheet sheet, int rowIndex, int columnIndex) {
        if (null == sheet) {
            return null;
        }

        Row row = sheet.getRow(rowIndex);
        if (null == row) {
            return null;
        }

        Cell cell = row.getCell(columnIndex);
        return cellValue(cell);
    }

    public Object getExcelRowCellValue(Row row, int columnIndex) {
        if (null == row) {
            return null;
        }

        Cell cell = row.getCell(columnIndex);
        return cellValue(cell);
    }

    public int[] getSheetRowColumnStartEnd(Sheet sheet) {
        if (null == sheet) {
            return null;
        }

        Integer rowEnd      = sheet.getLastRowNum();
        Integer rowStart    = sheet.getFirstRowNum();
        Integer columnStart = null;
        Integer columnEnd   = null;
        int     columnMax   = 500;
        int     columnBlankSize = 0;
        for (int i = rowStart; i <= rowEnd; i ++) {
            Row     row             = getExcelRow(sheet, i);
            Object  cellVal         = null;
            boolean cellNotEmpty    = false;

            for (int j = 0; j < columnMax; j ++) {
                cellVal     = getExcelRowCellValue(row, j);
                if (columnBlankSize > 26) { columnBlankSize = 0; break; }
                if (null == cellVal) { columnBlankSize ++; continue; }

                if (columnStart==null || columnStart>j) {
                    columnStart = j;
                }
                if (columnEnd==null || columnEnd<j) {
                    columnEnd = j;
                }
                columnBlankSize = 0;

                if (!cellNotEmpty && null!=cellVal) {
                    cellNotEmpty = true;
                }
            }
            if (!cellNotEmpty) { break; }


            if (rowStart==null || rowStart>i) {
                rowStart = i;
            }
            if (rowEnd==null || rowEnd<i) {
                rowEnd = i;
            }

        }
        if (null==rowStart) {
            return null;
        }
        if (null==rowEnd) {
            return null;
        }

        return new int[]{rowStart, rowEnd, columnStart, columnEnd};
    }

    public Object[][] getExcelArea(Sheet sheet, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        if (null==sheet ||
                rowStart<0 || rowEnd<0 ||
                columnStart<0 || columnEnd<0 ||
                rowEnd-rowStart<0 ||
                columnEnd-columnStart<0) {
            return null;
        }
        Object[][]  area    = new Object[rowEnd-rowStart+1][columnEnd-columnStart+1];
        for (int rowIdx = rowStart; rowIdx < rowEnd+1; rowIdx ++) {
            Row row =   getExcelRow(sheet, rowIdx);
            for (int colIdx = columnStart; colIdx < columnEnd+1; colIdx ++) {
                area[rowIdx-rowStart][colIdx-columnStart] = getExcelRowCellValue(row, colIdx);
            }
        }
        return area;
    }

    public Cell[][] getExcelCellArea(Sheet sheet, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        if (null==sheet ||
                rowStart<0 || rowEnd<0 ||
                columnStart<0 || columnEnd<0 ||
                rowEnd-rowStart<0 ||
                columnEnd-columnStart<0) {
            return null;
        }
        Cell[][]  area    = new Cell[rowEnd-rowStart+1][columnEnd-columnStart+1];
        for (int rowIdx = rowStart; rowIdx < rowEnd+1; rowIdx ++) {
            Row row =   getExcelRow(sheet, rowIdx);
            for (int colIdx = columnStart; colIdx < columnEnd+1; colIdx ++) {
                area[rowIdx-rowStart][colIdx-columnStart] = row.getCell(colIdx);
            }
        }
        return area;
    }

    public Integer[] getExcelContentRowIndex(Cell[][] area, String content, boolean like, boolean startWith) {
        if (null==area || null==content) { return null; }

        List<Integer> result = new ArrayList<>();

        for (int row = 0; row < area.length; row++) {
            Cell[] rowContent = area[row];

            boolean isMatch = false;
            for (int col = 0; col < rowContent.length; col ++) {
                Cell cell = rowContent[col];
                if (!(cellValue(cell) instanceof String)) { continue; }

                String string = (String) cellValue(cell);
                if (!like && !startWith) {
                    if (!content.equals(string)) { continue; }
                }
                else if (like && !startWith){
                    if (!stringUtil.like(string, content)) { continue; }
                }
                else if (!like && startWith) {
                    if (!string.startsWith(content)) { continue; }
                }
                else { continue; }

                isMatch = true;
                break;
            }
            if (isMatch) {
                result.add(row);
            }
        }

        return result.toArray(new Integer[result.size()]);
    }

    public Integer[] getExcelContentColumnIndex(Cell[][] area, String content[], boolean like, boolean startWith) {
        if (null==area || null==content) { return null; }

        Integer[] result = new Integer[content.length];

        for (int row = 0; row < area.length; row++) {
            Cell[] rowContent = area[row];

            for (int col = 0; col < rowContent.length; col ++) {
                Cell cell = rowContent[col];
                if (!(cellValue(cell) instanceof String)) { continue; }

                String string = (String) cellValue(cell);

                for (int contentIndex = 0; contentIndex < content.length; contentIndex ++) {
                    String contentValue = content[contentIndex];

                    if (!like && !startWith) {
                        if (!contentValue.equals(string)) { continue; }
                    }
                    else if (like && !startWith){
                        if (!stringUtil.like(string, contentValue)) { continue; }
                    }
                    else if (!like && startWith) {
                        if (!string.startsWith(contentValue)) { continue; }
                    }
                    else { continue; }

                    result[contentIndex] = col;
                    break;
                }
            }
        }

        return result;
    }

    public Object cellValue(Cell cell) {
        if (null == cell) {
            return null;
        }

        Object val = null;
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            val = cell.getCellFormula();
        } else if (cellType == CellType.NUMERIC) {
            val = cell.getNumericCellValue();
        } else if (cellType == CellType.BOOLEAN) {
            val = cell.getBooleanCellValue();
        } else if (cellType == CellType.STRING) {
            val = cell.getStringCellValue();
        } else if (cellType == CellType.BLANK) {
            val = null;
        } else {
            val = cell.getRichStringCellValue();
        }
        return val;
    }

    public String cellFormatString(Cell cell, String[] colors, String[] colorsReplaced) {
        if (null == cell) {
            return null;
        }

        String val = null;
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.STRING) {
            Object obj = cell.getRichStringCellValue();
            if (obj instanceof XSSFRichTextString) {
                XSSFRichTextString xssfRTS = (XSSFRichTextString) obj;

                String      string          = "";
                int         indexOfRunStart = 0;
                CTRElt[]    runs            = xssfRTS.getCTRst().getRArray();

                int count = null==runs ? 0 : runs.length;
                if (0==count) { val = xssfRTS.getString(); }
                else {
                    for (int i = 0; i < count; i++) {
                        CTRElt run = runs[i];
                        XSSFFont font = xssfRTS.getFontAtIndex(indexOfRunStart);

                        XSSFColor   color           = null==font   ? null : font.getXSSFColor();
                        byte[]      bColor          = null==color  ? null : color.getRGB();
                        String      sColor          = null==bColor ? null : colorUtil.parseColor(bColor);
                        boolean     isClrReplaced   = false;
                        for (int clrIdx = 0, clrCount = null==colors ? 0 : colors.length; clrIdx < clrCount; clrIdx ++) {
                            if (null==sColor) { break; }
                            if (!sColor.equalsIgnoreCase(colors[clrIdx])) { continue; }

                            sColor = colorsReplaced[clrIdx];
                            isClrReplaced = true;
                            break;
                        }

                        if (isClrReplaced && null!=sColor) {
                            sColor = " font_color='"+sColor+"' ";
                        }
                        else {
                            sColor = "";
                        }

                        if (null != font && font.getBold() && font.getItalic()) {
                            string = string + "<style "+sColor+"bold='true' italic='true'>" + run.getT() + "</style>";
                        } else if (null != font && font.getBold()) {
                            string = string + "<style "+sColor+"bold='true'>" + run.getT() + "</style>";
                        } else if (null != font && font.getItalic()) {
                            string = string + "<style "+sColor+"italic='true'>" + run.getT() + "</style>";
                        } else {
                            if ("".equals(sColor)) {
                                string = string + run.getT();
                            }
                            else {
                                string = string + "<style "+sColor+">" + run.getT() + "</style>";
                            }
                        }
                        string = string.replace("\n", "<br/>");

                        indexOfRunStart += xssfRTS.getLengthOfFormattingRun(i);
                    }
                    val = string;
                }
            }
        }
        return val;
    }

    public void closeWorkbook(Workbook workbook) {
        if (null != workbook) {
            try { workbook.close(); } catch (Exception ex) {}
        }
    }
}
