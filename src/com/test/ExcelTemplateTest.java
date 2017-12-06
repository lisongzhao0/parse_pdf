package com.test;

import com.happy.gene.utility.OfficeFileUtil;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.util.*;

public class ExcelTemplateTest {

    private static String       sampleSize      = "14";
    private static String       dispatchor      = "田彦捷";
    private static String       dispatchorDate  = "2017.11.20";
    private static String       sender          = "应龙";
    private static String[]     productNames    = {"肿瘤基础0新增"};
    private static String[][]   value =
            {
                    {"1",  "肿瘤基础0新增", "HD3", "17500001", "TX00011562", "2017.11.11", "2017.11.17", "血卡", "男", "23", ""},
                    {"2",  "肿瘤基础0新增", "HD3", "17500002", "TX00007181", "2017.11.11", "2017.11.17", "血卡", "女", "23", ""},
                    {"3",  "肿瘤基础0新增", "HD3", "17500003", "TX00011561", "2017.11.11", "2017.11.17", "血卡", "男", "23", ""},
                    {"4",  "肿瘤基础0新增", "HD3", "17500004", "TX00011862", "2017.11.11", "2017.11.17", "血卡", "女", "23", ""},
                    {"5",  "肿瘤基础0新增", "HD3", "17500005", "TX00007062", "2017.11.11", "2017.11.17", "血卡", "男", "23", ""},
                    {"6",  "肿瘤基础0新增", "HD3", "17500006", "TX00011885", "2017.11.11", "2017.11.17", "血卡", "女", "23", ""},
                    {"7",  "肿瘤基础0新增", "HD3", "17500007", "TX00011662", "2017.11.11", "2017.11.17", "血卡", "男", "23", ""},
                    {"8",  "肿瘤基础0新增", "HD3", "17500008", "TX00007219", "2017.11.11", "2017.11.17", "血卡", "女", "23", ""},
                    {"9",  "肿瘤基础0新增", "HD3", "17500009", "TX00011208", "2017.11.11", "2017.11.17", "血卡", "男", "23", ""},
                    {"10", "肿瘤基础0新增", "HD3", "17500010", "TX00011697", "2017.11.11", "2017.11.17", "血卡", "女", "23", ""},
                    {"11", "肿瘤基础0新增", "HD3", "17500011", "TX00011971", "2017.11.11", "2017.11.17", "血卡", "男", "23", ""},
                    {"12", "肿瘤基础0新增", "HD3", "17500012", "TX00011400", "2017.11.11", "2017.11.17", "血卡", "女", "23", ""},
                    {"13", "肿瘤基础0新增", "HD3", "17500013", "TX00011914", "2017.11.11", "2017.11.17", "血卡", "男", "23", ""},
                    {"14", "肿瘤基础0新增", "HD3", "17500014", "TX00011939", "2017.11.11", "2017.11.17", "血卡", "女", "23", ""}
            };

    public static void main(String[] args) {

        List<String> productOrds = Arrays.asList(new String[]{
                "17500001",
                "17500003",
                "17500004",
                "17500005",
                "17500002"
        });
        Collections.sort(productOrds, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (null==o1 && null==o2) { return 0; }
                if (null!=o1 && null==o2) { return 1; }
                if (null==o1 && null!=o2) { return -1; }
                return o1.compareTo(o2);
            }
        });
        System.out.println(productOrds);

        OfficeFileUtil officeFileUtil = OfficeFileUtil.newInstance();
        Workbook    workbook    = officeFileUtil.getExcel("/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/template_beijing_lihuafenxiceshi_center/dispatch_sample_table.xlsx");
        try
        {

            //读取工作表
            Sheet sheet = workbook.getSheetAt(0);
            final int[] range = officeFileUtil.getSheetRowColumnStartEnd(sheet);
            final Cell[][] cells = officeFileUtil.getExcelCellArea(sheet, range[0], range[1], range[2], range[3]);
            final Integer[][] sampleSizeColIndex        = officeFileUtil.getExcelContentRowColIndex(cells, "sampleSize", true, false, true);
            final Integer[][] dispatchorRowColIndex     = officeFileUtil.getExcelContentRowColIndex(cells, "dispatchor", true, false, true);
            final Integer[][] dispatchorDateRowColIndex = officeFileUtil.getExcelContentRowColIndex(cells, "dispatchDate", true, false, true);
            final Integer[][] senderRowColIndex         = officeFileUtil.getExcelContentRowColIndex(cells, "sender", true, false, true);

            sheet.getRow(sampleSizeColIndex[0][0]).getCell(sampleSizeColIndex[0][1]).setCellType(CellType.STRING);
            sheet.getRow(sampleSizeColIndex[0][0]).getCell(sampleSizeColIndex[0][1]).setCellValue(sampleSize);

            sheet.getRow(dispatchorRowColIndex[0][0]).getCell(dispatchorRowColIndex[0][1]).setCellType(CellType.STRING);
            sheet.getRow(dispatchorRowColIndex[0][0]).getCell(dispatchorRowColIndex[0][1]).setCellValue(dispatchor);

            sheet.getRow(dispatchorDateRowColIndex[0][0]).getCell(dispatchorDateRowColIndex[0][1]).setCellType(CellType.STRING);
            sheet.getRow(dispatchorDateRowColIndex[0][0]).getCell(dispatchorDateRowColIndex[0][1]).setCellValue(dispatchorDate);

            sheet.getRow(senderRowColIndex[0][0]).getCell(senderRowColIndex[0][1]).setCellType(CellType.STRING);
            sheet.getRow(senderRowColIndex[0][0]).getCell(senderRowColIndex[0][1]).setCellValue(sender);

            Row     row         = null;
            Cell    cell        = null;
            int     rowIndex    = 5;
            for(int i=0; i<value.length; i++)
            {
                row = sheet.createRow(rowIndex);
                //该行以前得部分从模板中取得;
                for (int j=0; j<value[0].length; j++)
                {
                    cell = row.createCell((short) j,CellType.STRING);
                    cell.setCellValue(value[i][j]);
                }

                rowIndex++;
            }
            FileOutputStream out = new FileOutputStream("/Users/zhaolisong/Documents/dispatch_sample_table_output.xlsx");
            workbook.write(out);
            out.flush();
            out.close();
        }
        catch(Exception e)
        { e.printStackTrace(); }
    }
}
