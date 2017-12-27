package com.test;

import com.happy.gene.utility.ColorUtil;
import com.happy.gene.utility.NumberUtil;
import com.happy.gene.utility.OfficeFileUtil;
import com.happy.gene.utility.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XinAnResultSuggestionExcelReader {

    public static final String DEFAULT_COLOR = "#ffffff";

    public static void main(String[] args) {
        NumberUtil numberUtil     = NumberUtil.newInstance();
        StringUtil stringUtil     = StringUtil.newInstance();
        ColorUtil  colorUtil      = ColorUtil.newInstance();



        OfficeFileUtil officeFileUtil = OfficeFileUtil.newInstance();
        Workbook ob = officeFileUtil.getExcel("/Users/zhaolisong/Desktop/baby_check_happygene/心安各位点对照数据库.xlsx");
        Cell[][] cells = officeFileUtil.getExcelCellArea(ob.getSheetAt(1));

        Pattern p = Pattern.compile("[^A-Za-z\\d]+");
        Map<String, String> gene = new HashMap<>();
        StringBuilder tmp = new StringBuilder();

        for (int r=0, rs=cells.length, lastRowCell=cells[0].length-1; r < rs; r ++) {
            String s = cells[r][lastRowCell].toString();
            s = s.replace("<br/>", "");
            Matcher m = p.matcher(s);
            s = m.replaceAll("\t");
            if (null != s && !"".equals(s)) {
                String[] sl = s.split("\t");
                for (String e : sl) {
                    if (stringUtil.isEmpty(e)) { continue; }
                    if (e.matches("\\d+")) { continue; }
                    gene.put(e, "<style italic=\"true\">"+e+"</style>");
                }
            }
        }



        int     groupIndex = -1;
        Boolean groupChange= null;
        for (int r=0, rs=cells.length; r < rs; r ++) {
            if (!stringUtil.isEmpty(cells[r][2].toString())) { groupIndex++; groupChange=true; }

            for (int c=0, cs=cells[0].length; c < cs; c ++) {
                if (c+1==cs) {
                    String detail = cells[r][c].toString();
                    for (String key : gene.keySet()) {
                        if (!detail.contains(key)) { continue; }
                        detail = detail.replace(key, gene.get(key));
                    }
                    tmp.append(detail);
                }
                else if (c==0) {
                    if (r==0) { tmp.append("组编号").append('\t'); }
                    else {
                        if (null!=groupChange && groupChange) { tmp.append(groupIndex).append('\t'); groupChange=null; }
                        else { tmp.append("").append('\t'); }
                    }

                    tmp.append(cells[r][c].toString()).append('\t');

                    XSSFCellStyle cellStyle8= (XSSFCellStyle) cells[r][c].getCellStyle();
                    byte[]        rgb8      = null==cellStyle8.getFillForegroundColorColor() ? null : ((XSSFColor)cellStyle8.getFillForegroundColorColor()).getRGB();
                    String        color8    = null==rgb8 ? null : colorUtil.parseColor(rgb8);
                    if (null==rgb8) {
                        if (r!=0) {
                            tmp.append(DEFAULT_COLOR).append('\t');
                        }
                        else {
                            tmp.append("编号颜色").append('\t');
                        }
                    }
                    else {
                        tmp.append(color8).append('\t');
                    }
                }
                else if (c==1) {
                    String sug = cells[r][c].toString();
                    tmp.append(sug).append('\t');

                    XSSFCellStyle cellStyle8= (XSSFCellStyle) cells[r][c].getCellStyle();
                    byte[]        rgb8      = null==cellStyle8.getFillForegroundColorColor() ? null : ((XSSFColor)cellStyle8.getFillForegroundColorColor()).getRGB();
                    String        color8    = null==rgb8 ? null : colorUtil.parseColor(rgb8);
                    if (null==rgb8) {
                        if (r==0) {
                            tmp.append("用药建议颜色").append('\t');
                        }
                        else {
                            if (!stringUtil.isEmpty(sug)) {
                                tmp.append(DEFAULT_COLOR).append('\t');
                            }
                            else {
                                tmp.append("").append('\t');
                            }
                        }
                    }
                    else {
                        if (!stringUtil.isEmpty(sug)) {
                            tmp.append(color8).append('\t');
                        }
                        else {
                            tmp.append("").append('\t');
                        }
                    }
                }
            }
            System.out.println(tmp.toString());
            tmp.setLength(0);
        }
    }
}
