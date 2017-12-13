package com.test;

import com.happy.gene.utility.OfficeFileUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ReadYuanMengProduct {

    public static void main(String[] args) {
        OfficeFileUtil officeFile = OfficeFileUtil.newInstance();

        Workbook workbook = officeFile.getExcel("/Users/zhaolisong/Downloads/远盟/套餐名称和检测位点信息（2017-12-07 邮件）.xlsx");
        int sheetSize = officeFile.getExcelSheetSize(workbook);
        for (int i = 0; i < sheetSize; i ++) {
            Sheet sheet = officeFile.getExcelSheet(workbook, i);

            System.out.println(sheet.getSheetName());

            int[] rowColumnStartEnd = officeFile.getSheetRowColumnStartEnd(sheet);
            Object[][] cells = officeFile.getExcelArea(sheet, rowColumnStartEnd[0], rowColumnStartEnd[1], rowColumnStartEnd[2], rowColumnStartEnd[3]);


            String col1 = null;
            String col2 = null;
            String col3 = null;
            String col4 = null;
            String col5 = null;
            boolean rowNotNull = false;
            StringBuilder row = new StringBuilder();
            for (int r = 0; r < cells.length; r ++) {
                rowNotNull = false;
                for (int c = 0; c < cells[0].length; c ++) {
                    Object val = cells[r][c];
                    rowNotNull = rowNotNull || (null!=val);
                    if (null!=cells[r][c] && c==0) {
                        col1 = (String)cells[r][c];
                    }
                    if (null!=cells[r][c] && c==1) {
                        col2 = (String)cells[r][c];
                    }
                    if (null!=cells[r][c] && c==2) {
                        col3 = (String)cells[r][c];
                    }
                    if (null!=cells[r][c] && c==3) {
                        col4 = (String)cells[r][c];
                    }
                    if (null!=cells[r][c] && c==4) {
                        col5 = (String)cells[r][c];
                    }
                    if (null==val) {
                        if (c==0) {
                            val = col1;
                        }
                        if (c==1) {
                            val = col2;
                        }
                        if (c==2) {
                            val = col3;
                        }
                        if (c==3) {
                            val = col4;
                        }
                        if (c==4) {
                            val = col5;
                        }
                    }
                    if (null!=val) {
                        row.append("[" + val.toString().replace("\n", "") + "]");
                    }
                    else {
                        row.append("[N/A]");
                    }
                }
                if (rowNotNull) {
                    System.out.println(row.toString());
                }
                row.setLength(0);
            }
            System.out.println("\n\n\n\n\n");
        }
    }
}
