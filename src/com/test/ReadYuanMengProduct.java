package com.test;

import com.happy.gene.utility.OfficeFileUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ReadYuanMengProduct {

    public static void main(String[] args) {
        OfficeFileUtil officeFile = OfficeFileUtil.newInstance();

        Workbook workbook = officeFile.getExcel("/Users/zhaolisong/Downloads/远盟产品 - 计算配置文件/远盟产品/LHA021 丽人守护--原有方式.xlsx");
        int sheetSize = officeFile.getExcelSheetSize(workbook);
        for (int i = 0; i < sheetSize; i ++) {
            Sheet sheet = officeFile.getExcelSheet(workbook, i);

            System.out.println(sheet.getSheetName());

            int[] rowColumnStartEnd = officeFile.getSheetRowColumnStartEnd(sheet);
            Object[][] cells = officeFile.getExcelArea(sheet, rowColumnStartEnd[0], rowColumnStartEnd[1], rowColumnStartEnd[2], rowColumnStartEnd[3]);


            String col1 = null, col2 = null, col3 = null, col4 = null, col5 = null;
            boolean rowNotNull       = false;
            StringBuilder row        = new StringBuilder();
            String[]      rowCopy    = new String[cells[0].length];
            String[]      rowPreCopy = new String[cells[0].length];
            for (int r = 0; r < cells.length; r ++) {
                rowNotNull = false;
                for (int c = 0; c < cells[0].length; c ++) {
                    Object val = cells[r][c];
                    rowNotNull = rowNotNull || (null!=val);
                    if (null!=cells[r][c] && c==0) { col1 = (String)cells[r][c]; }
                    if (null!=cells[r][c] && c==1) { col2 = (String)cells[r][c]; }
                    if (null!=cells[r][c] && c==2) { col3 = (String)cells[r][c]; }
                    if (null!=cells[r][c] && c==3) { col4 = (String)cells[r][c]; }
                    if (null!=cells[r][c] && c==4) { col5 = (String)cells[r][c]; }
                    if (null==val) {
                        val = rowPreCopy[c];
                    }
                    if (null!=col4 && null!=rowPreCopy[3] && !col4.equals(rowPreCopy[3]) &&
                        null!=col5 && null!=rowPreCopy[4] && !col5.equals(rowPreCopy[4])) {
                        StringBuilder tmp = new StringBuilder();
                        String        tmpV= null;
                        for (int idx = 0; idx < rowPreCopy.length; idx++) {
                            tmpV = rowPreCopy[idx];
                            if (idx==5) { tmpV = "0"; }
                            if (idx==6) { tmpV = "--"; }
                            if (idx==7) { tmpV = "--"; }
                            if (idx==8) { tmpV = "1.0"; }
                            tmp.append("["+tmpV+"]");
                            rowPreCopy[idx] = null;
                        }
                        System.out.println(tmp);
                    }

                    String cv = "N/A";
                    if (null!=val) {
                        cv = val.toString().replace("\n", "").trim();
                    }
                    row.append("["+cv+"]");

                    rowCopy[c] = cv;
                }
                if (rowNotNull) {
                    System.out.println(row.toString());
                    System.arraycopy(rowCopy, 0, rowPreCopy, 0, rowCopy.length);
                }

                col1 = col2 = col3 = col4 = col5 = null;
                row.setLength(0);
            }
            System.out.println("\n\n\n\n\n");
        }
    }
}
